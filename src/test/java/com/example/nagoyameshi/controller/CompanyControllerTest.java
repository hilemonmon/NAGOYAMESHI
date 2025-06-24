package com.example.nagoyameshi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.example.nagoyameshi.repository.CompanyRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

/**
 * {@link CompanyController} のアクセス制御と表示を検証するテストクラス。
 */
@WebMvcTest(CompanyController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyRepository companyRepository;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("未ログインでも会社概要ページが表示できる")
    void 未ログインでも会社概要ページが表示できる() throws Exception {
        when(companyRepository.findFirstByOrderByIdDesc())
                .thenReturn(Optional.of(Company.builder().id(1L).build()));

        mockMvc.perform(get("/company"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/index"));
    }

    @Test
    @DisplayName("一般ユーザーは会社概要ページを表示できる")
    void 一般ユーザーは会社概要ページを表示できる() throws Exception {
        when(companyRepository.findFirstByOrderByIdDesc())
                .thenReturn(Optional.of(Company.builder().id(1L).build()));

        // テスト用のユーザー情報を用意し、アプリと同じ UserDetailsImpl を生成する
        var userEntity = com.example.nagoyameshi.entity.User.builder()
                .id(1L)
                .name("侍 太郎")
                .email("taro.samurai@example.com")
                .build();
        UserDetailsImpl principal = new UserDetailsImpl(userEntity,
                List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));

        mockMvc.perform(get("/company").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("company/index"));
    }

    @Test
    @DisplayName("管理者が会社概要ページにアクセスすると403エラー")
    void 管理者は会社概要ページを閲覧できない() throws Exception {
        when(companyRepository.findFirstByOrderByIdDesc())
                .thenReturn(Optional.of(Company.builder().id(1L).build()));

        mockMvc.perform(get("/company").with(user("hanako.samurai@example.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }
}
