package com.example.nagoyameshi.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.form.CompanyEditForm;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.CompanyService;

/**
 * {@link AdminCompanyController} の動作を検証するテストクラス。
 */
@WebMvcTest(AdminCompanyController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class AdminCompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    // --- 表示ページのテスト -----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は会社概要ページからログインページにリダイレクトされる")
    void 未ログインの会社概要アクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/company"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは会社概要ページにアクセスすると403エラー")
    void 一般ユーザーは会社概要ページにアクセスできない() throws Exception {
        when(companyService.findFirstCompanyByOrderByIdDesc())
                .thenReturn(Optional.of(Company.builder().id(1L).build()));

        mockMvc.perform(get("/admin/company").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は会社概要ページを閲覧できる")
    void 管理者は会社概要ページを閲覧できる() throws Exception {
        when(companyService.findFirstCompanyByOrderByIdDesc())
                .thenReturn(Optional.of(Company.builder().id(1L).build()));

        mockMvc.perform(get("/admin/company").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/company/index"));
    }

    // --- 編集ページのテスト -----------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は編集ページからログインページにリダイレクトされる")
    void 未ログインの編集ページアクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin/company/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは編集ページにアクセスすると403エラー")
    void 一般ユーザーは編集ページにアクセスできない() throws Exception {
        when(companyService.findFirstCompanyByOrderByIdDesc())
                .thenReturn(Optional.of(Company.builder().id(1L).build()));

        mockMvc.perform(get("/admin/company/edit").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は編集ページを閲覧できる")
    void 管理者は編集ページを閲覧できる() throws Exception {
        when(companyService.findFirstCompanyByOrderByIdDesc())
                .thenReturn(Optional.of(Company.builder().id(1L).build()));

        mockMvc.perform(get("/admin/company/edit").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/company/edit"));
    }

    // --- 更新処理のテスト -------------------------------------------------------

    @Test
    @DisplayName("未ログインの場合は会社概要を更新できずログインページにリダイレクトされる")
    void 未ログインの更新はログインにリダイレクト() throws Exception {
        mockMvc.perform(post("/admin/company/update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは会社概要を更新できず403エラー")
    void 一般ユーザーは会社概要を更新できない() throws Exception {
        mockMvc.perform(post("/admin/company/update").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は会社概要を更新後にページへリダイレクトされる")
    void 管理者は会社概要を更新後にリダイレクトされる() throws Exception {
        when(companyService.updateCompany(any(CompanyEditForm.class)))
                .thenReturn(Company.builder().id(1L).build());

        mockMvc.perform(post("/admin/company/update")
                        .with(user("admin").roles("ADMIN"))
                        .param("name", "company")
                        .param("postalCode", "1234567")
                        .param("address", "addr")
                        .param("representative", "rep")
                        .param("establishmentDate", "date")
                        .param("capital", "cap")
                        .param("business", "biz")
                        .param("numberOfEmployees", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/company"));
    }
}
