package com.example.nagoyameshi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.RestaurantService;

/**
 * {@link RestaurantController} のアクセス権限を確認するテストクラス。
 */
@WebMvcTest(RestaurantController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    /** 未ログインでもページが表示できることを確認 */
    @Test
    @DisplayName("未ログインでも店舗一覧ページを閲覧できる")
    void 未ログインでも店舗一覧ページを閲覧できる() throws Exception {
        when(restaurantService.findAllRestaurantsByOrderByCreatedAtDesc(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 15), 0));
        when(categoryService.findAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/index"));
    }

    /** 一般ユーザー（無料会員）が閲覧できることを確認 */
    @Test
    @DisplayName("一般ユーザーは店舗一覧ページを閲覧できる")
    void 一般ユーザーは店舗一覧ページを閲覧できる() throws Exception {
        when(restaurantService.findAllRestaurantsByOrderByCreatedAtDesc(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 15), 0));
        when(categoryService.findAllCategories()).thenReturn(List.of());

        var userEntity = com.example.nagoyameshi.entity.User.builder()
                .id(1L)
                .name("侍 太郎")
                .email("taro.samurai@example.com")
                .build();
        UserDetailsImpl principal = new UserDetailsImpl(userEntity, List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_FREE_MEMBER")));

        mockMvc.perform(get("/restaurants").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/index"));
    }

    /** 管理者ユーザーはアクセスできないことを確認 */
    @Test
    @DisplayName("管理者ユーザーは店舗一覧ページにアクセスできない")
    void 管理者ユーザーは店舗一覧ページにアクセスできない() throws Exception {
        mockMvc.perform(get("/restaurants").with(user("hanako.samurai@example.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    /** 未ログインでも店舗詳細ページを閲覧できることを確認 */
    @Test
    @DisplayName("未ログインでも店舗詳細ページを閲覧できる")
    void 未ログインでも店舗詳細ページを閲覧できる() throws Exception {
        var restaurant = Restaurant.builder()
                .id(1L)
                .categoriesRestaurants(List.of())
                .regularHolidaysRestaurants(List.of())
                .reviews(List.of())
                .build();
        when(restaurantService.findRestaurantById(1L)).thenReturn(java.util.Optional.of(restaurant));

        mockMvc.perform(get("/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/show"));
    }

    /** 一般ユーザー（無料会員）は店舗詳細ページを閲覧できることを確認 */
    @Test
    @DisplayName("一般ユーザーは店舗詳細ページを閲覧できる")
    void 一般ユーザーは店舗詳細ページを閲覧できる() throws Exception {
        var restaurant = Restaurant.builder()
                .id(1L)
                .categoriesRestaurants(List.of())
                .regularHolidaysRestaurants(List.of())
                .reviews(List.of())
                .build();
        when(restaurantService.findRestaurantById(1L)).thenReturn(java.util.Optional.of(restaurant));

        var userEntity = com.example.nagoyameshi.entity.User.builder()
                .id(1L)
                .name("侍 太郎")
                .email("taro.samurai@example.com")
                .build();
        UserDetailsImpl principal = new UserDetailsImpl(userEntity, List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_FREE_MEMBER")));

        mockMvc.perform(get("/restaurants/1").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/show"));
    }

    /** 管理者ユーザーは店舗詳細ページにアクセスできないことを確認 */
    @Test
    @DisplayName("管理者ユーザーは店舗詳細ページにアクセスできない")
    void 管理者ユーザーは店舗詳細ページにアクセスできない() throws Exception {
        mockMvc.perform(get("/restaurants/1").with(user("hanako.samurai@example.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }
}
