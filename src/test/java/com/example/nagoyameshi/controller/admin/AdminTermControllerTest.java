package com.example.nagoyameshi.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Term;
import com.example.nagoyameshi.form.TermEditForm;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.TermService;

/**
 * {@link AdminTermController} の動作を検証するテストクラス。
 */
@WebMvcTest(AdminTermController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class AdminTermControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TermService termService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    // --- 表示ページのテスト -----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は利用規約ページからログインページにリダイレクトされる")
    void 未ログインの利用規約アクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/terms"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは利用規約ページにアクセスすると403エラー")
    void 一般ユーザーは利用規約ページにアクセスできない() throws Exception {
        when(termService.findFirstTermByOrderByIdDesc())
                .thenReturn(Optional.of(Term.builder().id(1L).build()));

        mockMvc.perform(get("/admin/terms").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は利用規約ページを閲覧できる")
    void 管理者は利用規約ページを閲覧できる() throws Exception {
        when(termService.findFirstTermByOrderByIdDesc())
                .thenReturn(Optional.of(Term.builder().id(1L).build()));

        mockMvc.perform(get("/admin/terms").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/terms/index"));
    }

    // --- 編集ページのテスト -----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は利用規約編集ページからログインページにリダイレクトされる")
    void 未ログインの規約編集アクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/terms/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは利用規約編集ページにアクセスすると403エラー")
    void 一般ユーザーは規約編集ページにアクセスできない() throws Exception {
        when(termService.findFirstTermByOrderByIdDesc())
                .thenReturn(Optional.of(Term.builder().id(1L).build()));

        mockMvc.perform(get("/admin/terms/edit").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は利用規約編集ページを閲覧できる")
    void 管理者は規約編集ページを閲覧できる() throws Exception {
        when(termService.findFirstTermByOrderByIdDesc())
                .thenReturn(Optional.of(Term.builder().id(1L).build()));

        mockMvc.perform(get("/admin/terms/edit").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/terms/edit"));
    }

    // --- 更新処理のテスト -------------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は利用規約を更新できずログインページにリダイレクトされる")
    void 未ログインの規約更新はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/terms/update").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは利用規約を更新できず403エラー")
    void 一般ユーザーは規約更新できない() throws Exception {
        mockMvc.perform(post("/admin/terms/update").with(user("user").roles("FREE_MEMBER")).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は利用規約を更新後にページへリダイレクトされる")
    void 管理者は規約更新後にリダイレクトされる() throws Exception {
        when(termService.updateTerm(any(TermEditForm.class)))
                .thenReturn(Term.builder().id(1L).build());

        mockMvc.perform(post("/admin/terms/update")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                        .param("content", "text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/terms"));
    }
}
