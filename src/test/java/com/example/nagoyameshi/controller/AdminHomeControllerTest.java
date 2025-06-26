package com.example.nagoyameshi.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.UserService;

/**
 * {@link AdminHomeController} の表示処理を検証するテスト。
 */
@WebMvcTest(AdminHomeController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class AdminHomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("未ログインの場合は管理画面からログインページにリダイレクトされる")
    void 未ログインの管理画面アクセスはログインにリダイレクト() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("一般ユーザーは管理画面にアクセスすると403エラー")
    void 一般ユーザーは管理画面にアクセスできない() throws Exception {
        mockMvc.perform(get("/admin").with(user("user").roles("FREE_MEMBER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("管理者は管理画面を閲覧できる")
    void 管理者は管理画面を閲覧できる() throws Exception {
        // 各カウント処理は0を返すようにモック
        org.mockito.Mockito.when(userService.countUsersByRole_Name(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(0L);
        org.mockito.Mockito.when(restaurantService.countRestaurants()).thenReturn(0L);
        org.mockito.Mockito.when(reservationService.countReservations()).thenReturn(0L);

        mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"));
    }
}
