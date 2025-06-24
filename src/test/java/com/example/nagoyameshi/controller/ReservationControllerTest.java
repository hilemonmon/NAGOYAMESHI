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

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.security.UserDetailsServiceImpl;
import com.example.nagoyameshi.security.WebSecurityConfig;
import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.RestaurantService;

/**
 * {@link ReservationController} のアクセス権限を検証するテストクラス。
 */
@WebMvcTest(ReservationController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

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
                .openingTime(LocalTime.of(10, 0))
                .closingTime(LocalTime.of(20, 0))
                .reservations(List.of())
                .build();
    }

    private Reservation dummyReservation(User user) {
        return Reservation.builder()
                .id(1L)
                .reservedDatetime(LocalDate.now().atTime(LocalTime.NOON))
                .numberOfPeople(5)
                .restaurant(dummyRestaurant())
                .user(user)
                .build();
    }

    /** 未ログインはログインページへリダイレクトされる */
    @Test
    @DisplayName("未ログインは予約一覧ページにアクセスできない")
    void 未ログインは予約一覧ページにアクセスできない() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    /** 無料会員は予約一覧にアクセスすると有料登録へ */
    @Test
    @DisplayName("無料会員は予約一覧ページから有料プランページへリダイレクト")
    void 無料会員は予約一覧にアクセスできない() throws Exception {
        User user = User.builder().id(1L).email("free@example.com").build();
        UserDetailsImpl principal = new UserDetailsImpl(user, List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));
        when(reservationService.findReservationsByUserOrderByReservedDatetimeDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 15), 0));
        mockMvc.perform(get("/reservations").with(user(principal)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));
    }

    /** 有料会員は予約一覧を閲覧できる */
    @Test
    @DisplayName("有料会員は予約一覧を閲覧できる")
    void 有料会員は予約一覧を閲覧できる() throws Exception {
        User user = User.builder().id(1L).email("paid@example.com").build();
        UserDetailsImpl principal = new UserDetailsImpl(user, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        when(reservationService.findReservationsByUserOrderByReservedDatetimeDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dummyReservation(user)), PageRequest.of(0, 15), 1));
        mockMvc.perform(get("/reservations").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("reservations/index"));
    }

    /** 管理者は予約一覧にアクセスできない */
    @Test
    @DisplayName("管理者は予約一覧ページにアクセスできない")
    void 管理者は予約一覧ページにアクセスできない() throws Exception {
        mockMvc.perform(get("/reservations").with(user("admin").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    /** 予約ページのアクセス制御を確認 */
    @Test
    @DisplayName("予約ページへのアクセス権限を検証")
    void 予約ページアクセス制限() throws Exception {
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(dummyRestaurant()));
        when(restaurantService.findRegularHolidayDayIndicesByRestaurantId(1L)).thenReturn(List.of());

        // 未ログイン
        mockMvc.perform(get("/restaurants/1/reservations/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // 無料会員
        User freeUser = User.builder().id(1L).email("free@example.com").build();
        UserDetailsImpl freePrincipal = new UserDetailsImpl(freeUser, List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));
        mockMvc.perform(get("/restaurants/1/reservations/register").with(user(freePrincipal)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));

        // 有料会員
        User paidUser = User.builder().id(2L).email("paid@example.com").build();
        UserDetailsImpl paidPrincipal = new UserDetailsImpl(paidUser, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        mockMvc.perform(get("/restaurants/1/reservations/register").with(user(paidPrincipal)))
                .andExpect(status().isOk())
                .andExpect(view().name("reservations/register"));

        // 管理者
        mockMvc.perform(get("/restaurants/1/reservations/register").with(user("admin").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    /** 予約作成のアクセス制御 */
    @Test
    @DisplayName("予約作成時の権限制御を検証")
    void 予約作成権限制御() throws Exception {
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(dummyRestaurant()));
        when(restaurantService.findRegularHolidayDayIndicesByRestaurantId(1L)).thenReturn(List.of());
        when(reservationService.isAtLeastTwoHoursInFuture(any())).thenReturn(true);
        when(reservationService.createReservation(any(ReservationRegisterForm.class), any(Restaurant.class), any(User.class)))
                .thenReturn(dummyReservation(User.builder().id(2L).build()));

        // 未ログイン
        mockMvc.perform(post("/restaurants/1/reservations/create").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // 無料会員
        User freeUser = User.builder().id(1L).email("free@example.com").build();
        UserDetailsImpl freePrincipal = new UserDetailsImpl(freeUser, List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));
        mockMvc.perform(post("/restaurants/1/reservations/create").with(user(freePrincipal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));

        // 有料会員
        User paidUser = User.builder().id(2L).email("paid@example.com").build();
        UserDetailsImpl paidPrincipal = new UserDetailsImpl(paidUser, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        mockMvc.perform(post("/restaurants/1/reservations/create").with(user(paidPrincipal)).with(csrf())
                .param("reservationDate", "2030-01-01")
                .param("reservationTime", "12:00:00")
                .param("numberOfPeople", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations"));

        // 管理者
        mockMvc.perform(post("/restaurants/1/reservations/create").with(user("admin").roles("ADMIN")).with(csrf()))
                .andExpect(status().isForbidden());
    }

    /** 予約キャンセルのアクセス制御 */
    @Test
    @DisplayName("予約キャンセル時の権限制御を検証")
    void 予約キャンセル権限制御() throws Exception {
        User owner = User.builder().id(1L).email("owner@example.com").build();
        Reservation reservation = dummyReservation(owner);
        when(reservationService.findReservationById(1L)).thenReturn(Optional.of(reservation));

        // 未ログイン
        mockMvc.perform(post("/reservations/1/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // 無料会員
        User freeUser = User.builder().id(2L).email("free@example.com").build();
        UserDetailsImpl freePrincipal = new UserDetailsImpl(freeUser, List.of(new SimpleGrantedAuthority("ROLE_FREE_MEMBER")));
        mockMvc.perform(post("/reservations/1/delete").with(user(freePrincipal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/register"));

        // 有料会員(本人)
        UserDetailsImpl paidPrincipal = new UserDetailsImpl(owner, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        mockMvc.perform(post("/reservations/1/delete").with(user(paidPrincipal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations"));

        // 有料会員(他人)
        User another = User.builder().id(3L).email("other@example.com").build();
        UserDetailsImpl anotherPrincipal = new UserDetailsImpl(another, List.of(new SimpleGrantedAuthority("ROLE_PAID_MEMBER")));
        mockMvc.perform(post("/reservations/1/delete").with(user(anotherPrincipal)).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations"));

        // 管理者
        mockMvc.perform(post("/reservations/1/delete").with(user("admin").roles("ADMIN")).with(csrf()))
                .andExpect(status().isForbidden());
    }
}
