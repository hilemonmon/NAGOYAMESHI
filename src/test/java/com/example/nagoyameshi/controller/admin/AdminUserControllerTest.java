package com.example.nagoyameshi.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.AdminUserService;

/**
 * {@link AdminUserController} の動作を検証するテスト。
 */
@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserService adminUserService;

    @Test
    @DisplayName("GET /admin/users は一覧ページを返す")
    void 一覧ページが表示される() throws Exception {
        when(adminUserService.searchUsers(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/admin/users").param("keyword", "yamada").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/index"));
    }

    @Test
    @DisplayName("GET /admin/users/{id} は詳細ページを返す")
    void 詳細ページが表示される() throws Exception {
        when(adminUserService.getUserById(1L)).thenReturn(User.builder().id(1L).build());

        mockMvc.perform(get("/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/show"));
    }
}
