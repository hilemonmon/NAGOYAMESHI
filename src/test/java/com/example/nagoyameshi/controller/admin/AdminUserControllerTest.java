package com.example.nagoyameshi.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.AdminUserService;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;

/**
 * {@link AdminUserController} の動作を検証するテスト。
 */
@WebMvcTest(AdminUserController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserService adminUserService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("未ログインの場合は一覧ページからログインページにリダイレクトされる")
    void 未ログインの一覧ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは一覧ページにアクセスすると403エラー")
    void 一般ユーザーは一覧ページにアクセスできない() throws Exception {
        when(adminUserService.searchUsers(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/admin/users").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は一覧ページを閲覧できる")
    void 管理者は一覧ページを閲覧できる() throws Exception {
        when(adminUserService.searchUsers(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/admin/users").param("keyword", "yamada").param("page", "0")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/index"));
    }

    @Test
    @DisplayName("未ログインの場合は詳細ページからログインページにリダイレクトされる")
    void 未ログインの詳細ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/users/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは詳細ページにアクセスすると403エラー")
    void 一般ユーザーは詳細ページにアクセスできない() throws Exception {
        when(adminUserService.getUserById(1L)).thenReturn(
                User.builder()
                        .id(1L)
                        .role(Role.builder().name("ROLE_FREE_MEMBER").build())
                        .build());

        mockMvc.perform(get("/admin/users/1").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は詳細ページを閲覧できる")
    void 管理者は詳細ページを閲覧できる() throws Exception {
        when(adminUserService.getUserById(1L)).thenReturn(
                User.builder()
                        .id(1L)
                        .role(Role.builder().name("ROLE_FREE_MEMBER").build())
                        .build());

        mockMvc.perform(get("/admin/users/1").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/show"));
    }
}
