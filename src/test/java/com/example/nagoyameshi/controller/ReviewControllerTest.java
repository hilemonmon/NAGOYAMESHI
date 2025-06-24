package com.example.nagoyameshi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;

/**
 * {@link ReviewController} のアクセス権限を検証するテストクラス。
 */
@WebMvcTest(ReviewController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    private Restaurant dummyRestaurant() {
        return Restaurant.builder()
                .id(1L)
                .categoriesRestaurants(List.of())
                .regularHolidaysRestaurants(List.of())
                .reviews(List.of())
                .build();
    }

    /** 未ログインの場合はログインページへリダイレクトされる */
    @Test
    @DisplayName("未ログインの場合はログインページにリダイレクト")
    void 未ログインの場合はログインページにリダイレクト() throws Exception {
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(dummyRestaurant()));
        when(reviewService.findReviewsByRestaurantOrderByCreatedAtDesc(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 5), 0));

        mockMvc.perform(get("/restaurants/1/reviews"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    /** 無料会員は一覧を閲覧できるが投稿は有料プランページへ */
    @Test
    @DisplayName("無料会員はレビュー投稿ページへアクセスすると有料プランページにリダイレクト")
    void 無料会員は投稿ページへアクセスできない() throws Exception {
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(dummyRestaurant()));
        when(reviewService.findReviewsByRestaurantOrderByCreatedAtDesc(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 5), 0));

        User userEntity = User.builder().id(1L).name("侍 太郎").email("taro@example.com").build();
        UserDetailsImpl principal = new UserDetailsImpl(userEntity,
                List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));

        mockMvc.perform(get("/restaurants/1/reviews").with(user(principal)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("reviews/index"));

        mockMvc.perform(get("/restaurants/1/reviews/register").with(user(principal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));
    }

    /** 有料会員は投稿・編集・削除まで行える */
    @Test
    @DisplayName("有料会員はレビューを投稿・編集・削除できる")
    void 有料会員はレビューを投稿編集削除できる() throws Exception {
        Restaurant restaurant = dummyRestaurant();
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(restaurant));
        when(reviewService.findReviewsByRestaurantOrderByCreatedAtDesc(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 5), 0));
        Review review = Review.builder().id(10L).restaurant(restaurant).user(User.builder().id(1L).build())
                .content("good").score(5).build();
        when(reviewService.findReviewById(10L)).thenReturn(Optional.of(review));
        User userEntity = User.builder().id(1L).name("侍 太郎").email("taro@example.com").build();
        UserDetailsImpl principal = new UserDetailsImpl(userEntity,
                List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));

        mockMvc.perform(get("/restaurants/1/reviews/register").with(user(principal)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("reviews/register"));

        mockMvc.perform(post("/restaurants/1/reviews/create").with(user(principal)).with(csrf())
                .param("score", "5").param("content", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants/1/reviews"));

        mockMvc.perform(get("/restaurants/1/reviews/10/edit").with(user(principal)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("reviews/edit"));

        mockMvc.perform(post("/restaurants/1/reviews/10/update").with(user(principal)).with(csrf())
                .param("score", "4").param("content", "edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants/1/reviews"));

        mockMvc.perform(post("/restaurants/1/reviews/10/delete").with(user(principal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants/1/reviews"));
    }

    /** 他人のレビューは編集・削除できない */
    @Test
    @DisplayName("他人のレビュー編集削除は店舗詳細にリダイレクト")
    void 他人のレビューは操作できない() throws Exception {
        Restaurant restaurant = dummyRestaurant();
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(restaurant));
        Review review = Review.builder().id(10L).restaurant(restaurant).user(User.builder().id(2L).build())
                .content("good").score(5).build();
        when(reviewService.findReviewById(10L)).thenReturn(Optional.of(review));
        User userEntity = User.builder().id(1L).name("侍 太郎").email("taro@example.com").build();
        UserDetailsImpl principal = new UserDetailsImpl(userEntity,
                List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));

        mockMvc.perform(get("/restaurants/1/reviews/10/edit").with(user(principal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants/1"));

        mockMvc.perform(post("/restaurants/1/reviews/10/update").with(user(principal)).with(csrf())
                .param("score", "5").param("content", "edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants/1"));

        mockMvc.perform(post("/restaurants/1/reviews/10/delete").with(user(principal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants/1"));
    }

    /** 管理者はアクセス不可 */
    @Test
    @DisplayName("管理者はレビュー機能にアクセスできない")
    void 管理者はアクセスできない() throws Exception {
        mockMvc.perform(get("/restaurants/1/reviews").with(user("admin").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }
}
