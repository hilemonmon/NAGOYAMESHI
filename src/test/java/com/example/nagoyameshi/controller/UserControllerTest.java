package com.example.nagoyameshi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.security.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

/**
 * {@link UserController} の動作を検証するテストクラス。
 */
@WebMvcTest(UserController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    // --- 表示ページのテスト ----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は会員情報ページからログインページにリダイレクトされる")
    void 未ログインの会員情報アクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("未ログインの場合は会員情報編集ページからログインページにリダイレクトされる")
    void 未ログインの編集ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/user/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("ログイン済みの場合は会員情報ページを閲覧できる")
    void ログイン済みの場合は会員情報ページを閲覧できる() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("侍 太郎")
                .email("taro.samurai@example.com")
                .role(Role.builder().name("ROLE_FREE_MEMBER").build())
                .build();
        when(userService.findUserByEmail("taro.samurai@example.com")).thenReturn(user);
        UserDetailsImpl principal = new UserDetailsImpl(user,
                List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));

        mockMvc.perform(get("/user").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"));
    }

    @Test
    @DisplayName("ログイン済みの場合は会員情報編集ページを閲覧できる")
    void ログイン済みの場合は会員情報編集ページを閲覧できる() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("侍 太郎")
                .furigana("サムライ タロウ")
                .postalCode("1010022")
                .address("東京都千代田区")
                .phoneNumber("09012345678")
                .birthday(java.time.LocalDate.of(1990, 1, 1))
                .occupation("エンジニア")
                .email("taro.samurai@example.com")
                .role(Role.builder().name("ROLE_FREE_MEMBER").build())
                .build();
        when(userService.findUserByEmail("taro.samurai@example.com")).thenReturn(user);
        UserDetailsImpl principal = new UserDetailsImpl(user,
                List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));

        mockMvc.perform(get("/user/edit").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"));
    }

    // --- 更新処理のテスト ------------------------------------------------------

    @Test
    @DisplayName("ログイン済みの場合は会員情報を更新できる")
    void ログイン済みの場合は会員情報を更新できる() throws Exception {
        User currentUser = User.builder()
                .id(1L)
                .name("侍 太郎")
                .email("taro.samurai@example.com")
                .role(Role.builder().name("ROLE_FREE_MEMBER").build())
                .build();
        when(userService.findUserByEmail("taro.samurai@example.com")).thenReturn(currentUser);
        when(userService.isEmailChanged(any(UserEditForm.class), any(User.class))).thenReturn(false);
        when(userService.updateUser(any(UserEditForm.class), any(User.class))).thenReturn(currentUser);
        UserDetailsImpl principal = new UserDetailsImpl(currentUser,
                List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));

        mockMvc.perform(post("/user/update")
                .with(user(principal))
                .param("name", "侍 太郎")
                .param("furigana", "サムライ タロウ")
                .param("postalCode", "1010022")
                .param("address", "東京都千代田区")
                .param("phoneNumber", "09012345678")
                .param("birthday", "19900101")
                .param("occupation", "エンジニア")
                .param("email", "taro.samurai@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));
    }
}
