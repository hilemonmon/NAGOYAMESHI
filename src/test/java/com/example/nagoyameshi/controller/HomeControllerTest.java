package com.example.nagoyameshi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.service.VerificationTokenService;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.CategoryService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.data.domain.Page;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import com.example.nagoyameshi.entity.Category;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class HomeControllerTest {

    // UserService をモック化してテスト用のコンテキストに登録する
    @MockitoBean
    private UserService userService;

    // SignupEventPublisher をモック化
    @MockitoBean
    private SignupEventPublisher signupEventPublisher;

    // VerificationTokenService もモック化
    @MockitoBean
    private VerificationTokenService verificationTokenService;

    // RestaurantService もモック化
    @MockitoBean
    private RestaurantService restaurantService;

    // CategoryService もモック化
    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("未ログインの場合はトップページが正しく表示される")
    void 未ログインの場合はトップページが正しく表示される() throws Exception {
        when(restaurantService.getRestaurants(null)).thenReturn(List.of());
        when(restaurantService.findAllRestaurantsByOrderByCreatedAtDesc(any())).thenReturn(Page.empty());
        Category dummyCategory = Category.builder().id(1L).name("dummy").build();
        when(categoryService.findFirstCategoryByName(any())).thenReturn(Optional.of(dummyCategory));
        when(categoryService.findAllCategories()).thenReturn(List.of());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(username = "taro.samurai@example.com", roles = {"USER"})
    @DisplayName("ログイン済み一般ユーザーでもトップページが正しく表示される")
    void ログイン済み一般ユーザーでもトップページが正しく表示される() throws Exception {
        when(restaurantService.getRestaurants(null)).thenReturn(List.of());
        when(restaurantService.findAllRestaurantsByOrderByCreatedAtDesc(any())).thenReturn(Page.empty());
        Category dummyCategory = Category.builder().id(1L).name("dummy").build();
        when(categoryService.findFirstCategoryByName(any())).thenReturn(Optional.of(dummyCategory));
        when(categoryService.findAllCategories()).thenReturn(List.of());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}
