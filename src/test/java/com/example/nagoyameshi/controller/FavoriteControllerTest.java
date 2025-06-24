package com.example.nagoyameshi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.RestaurantService;

/**
 * {@link FavoriteController} のアクセス権限を検証するテストクラス。
 */
@WebMvcTest(FavoriteController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;

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
                .reservations(List.of())
                .favorites(List.of())
                .build();
    }

    private Favorite dummyFavorite(User user) {
        return Favorite.builder()
                .id(1L)
                .restaurant(dummyRestaurant())
                .user(user)
                .build();
    }

    /** 未ログインはログインページへリダイレクトされる */
    @Test
    @DisplayName("未ログインはお気に入り一覧にアクセスできない")
    void 未ログインはお気に入り一覧にアクセスできない() throws Exception {
        mockMvc.perform(get("/favorites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    /** 無料会員は有料登録ページへリダイレクト */
    @Test
    @DisplayName("無料会員はお気に入り一覧から有料プランページへリダイレクト")
    void 無料会員はお気に入り一覧アクセス不可() throws Exception {
        User user = User.builder().id(1L).email("free@example.com").build();
        UserDetailsImpl principal = new UserDetailsImpl(user, List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));
        when(favoriteService.findFavoritesByUserOrderByCreatedAtDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));
        mockMvc.perform(get("/favorites").with(user(principal)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));
    }

    /** 有料会員は一覧を閲覧できる */
    @Test
    @DisplayName("有料会員はお気に入り一覧を閲覧できる")
    void 有料会員はお気に入り一覧を閲覧できる() throws Exception {
        User user = User.builder().id(1L).email("paid@example.com").build();
        UserDetailsImpl principal = new UserDetailsImpl(user, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        when(favoriteService.findFavoritesByUserOrderByCreatedAtDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dummyFavorite(user)), PageRequest.of(0, 10), 1));
        mockMvc.perform(get("/favorites").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("favorites/index"));
    }

    /** 管理者はアクセスできない */
    @Test
    @DisplayName("管理者はお気に入り一覧にアクセスできない")
    void 管理者はお気に入り一覧にアクセスできない() throws Exception {
        mockMvc.perform(get("/favorites").with(user("admin").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    /** お気に入り追加の権限制御を検証 */
    @Test
    @DisplayName("お気に入り追加の権限制御を検証")
    void お気に入り追加権限制御() throws Exception {
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(dummyRestaurant()));
        when(favoriteService.createFavorite(any(Restaurant.class), any(User.class))).thenReturn(dummyFavorite(User.builder().id(2L).build()));

        // 未ログイン
        mockMvc.perform(post("/restaurants/1/favorites/create").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // 無料会員
        User freeUser = User.builder().id(1L).email("free@example.com").build();
        UserDetailsImpl freePrincipal = new UserDetailsImpl(freeUser, List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));
        mockMvc.perform(post("/restaurants/1/favorites/create").with(user(freePrincipal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));

        // 有料会員
        User paidUser = User.builder().id(2L).email("paid@example.com").build();
        UserDetailsImpl paidPrincipal = new UserDetailsImpl(paidUser, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        mockMvc.perform(post("/restaurants/1/favorites/create").with(user(paidPrincipal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants/1"));

        // 管理者
        mockMvc.perform(post("/restaurants/1/favorites/create").with(user("admin").roles("ADMIN")).with(csrf()))
                .andExpect(status().isForbidden());
    }

    /** お気に入り解除の権限制御を検証 */
    @Test
    @DisplayName("お気に入り解除の権限制御を検証")
    void お気に入り解除権限制御() throws Exception {
        User owner = User.builder().id(1L).email("owner@example.com").build();
        Favorite favorite = dummyFavorite(owner);
        when(favoriteService.findFavoriteById(1L)).thenReturn(Optional.of(favorite));

        // 未ログイン
        mockMvc.perform(post("/favorites/1/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // 無料会員
        User freeUser = User.builder().id(2L).email("free@example.com").build();
        UserDetailsImpl freePrincipal = new UserDetailsImpl(freeUser, List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));
        mockMvc.perform(post("/favorites/1/delete").with(user(freePrincipal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));

        // 有料会員(本人)
        UserDetailsImpl paidPrincipal = new UserDetailsImpl(owner, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        mockMvc.perform(post("/favorites/1/delete").with(user(paidPrincipal)).with(csrf()).header("Referer", "/favorites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/favorites"));

        // 有料会員(他人)
        User another = User.builder().id(3L).email("other@example.com").build();
        UserDetailsImpl anotherPrincipal = new UserDetailsImpl(another, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        mockMvc.perform(post("/favorites/1/delete").with(user(anotherPrincipal)).with(csrf()).header("Referer", "/favorites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/favorites"));

        // 管理者
        mockMvc.perform(post("/favorites/1/delete").with(user("admin").roles("ADMIN")).with(csrf()))
                .andExpect(status().isForbidden());
    }
}
