package com.example.nagoyameshi.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.RestaurantService;

import lombok.RequiredArgsConstructor;

/**
 * 会員向け予約機能を提供するコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final RestaurantService restaurantService;

    /** 予約一覧を表示する。 */
    @GetMapping("/reservations")
    public String index(
            @PageableDefault(page = 0, size = 15, sort = "id", direction = Direction.ASC) Pageable pageable,
            Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            return "redirect:/login"; // 未ログインはログインページへ
        }
        String role = principal.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role) && !"ROLE_FREE_MEMBER".equals(role)) {
            return "redirect:/"; // 管理者などはアクセス不可
        }
        if ("ROLE_FREE_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約機能の利用には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        User user = principal.getUser();
        Page<Reservation> reservationPage = reservationService.findReservationsByUserOrderByReservedDatetimeDesc(user,
                pageable);
        model.addAttribute("reservationPage", reservationPage);
        model.addAttribute("currentDateTime", LocalDateTime.now());
        return "reservations/index";
    }

    /** 予約ページを表示する。 */
    @GetMapping("/restaurants/{restaurantId}/reservations/register")
    public String register(@PathVariable Long restaurantId, ReservationRegisterForm form, Model model,
            RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            return "redirect:/login";
        }
        String role = principal.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role) && !"ROLE_FREE_MEMBER".equals(role)) {
            return "redirect:/";
        }
        if ("ROLE_FREE_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        var restaurantOpt = restaurantService.findRestaurantById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません。");
            return "redirect:/restaurants";
        }
        Restaurant restaurant = restaurantOpt.get();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurantRegularHolidays",
                restaurantService.findRegularHolidayDayIndicesByRestaurantId(restaurantId));
        return "reservations/register";
    }

    /** 予約を新規作成する。 */
    @PostMapping("/restaurants/{restaurantId}/reservations/create")
    public String create(@PathVariable Long restaurantId, @Validated ReservationRegisterForm form,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            return "redirect:/login";
        }
        String role = principal.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role) && !"ROLE_FREE_MEMBER".equals(role)) {
            return "redirect:/";
        }
        if ("ROLE_FREE_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        var restaurantOpt = restaurantService.findRestaurantById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません。");
            return "redirect:/restaurants";
        }
        Restaurant restaurant = restaurantOpt.get();
        if (!reservationService.isAtLeastTwoHoursInFuture(
                LocalDateTime.of(form.getReservationDate(), form.getReservationTime()))) {
            bindingResult.reject("timeError", "予約は2時間後以降の日時を選択してください。");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("restaurantRegularHolidays",
                    restaurantService.findRegularHolidayDayIndicesByRestaurantId(restaurantId));
            return "reservations/register";
        }
        User user = principal.getUser();
        reservationService.createReservation(form, restaurant, user);
        redirectAttributes.addFlashAttribute("successMessage", "予約が完了しました。");
        return "redirect:/reservations";
    }

    /** 予約をキャンセルする。 */
    @PostMapping("/reservations/{reservationId}/delete")
    public String delete(@PathVariable Long reservationId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            return "redirect:/login";
        }
        String role = principal.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role) && !"ROLE_FREE_MEMBER".equals(role)) {
            return "redirect:/";
        }
        if ("ROLE_FREE_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約のキャンセルには有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        var reservationOpt = reservationService.findReservationById(reservationId);
        if (reservationOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約が存在しません。");
            return "redirect:/reservations";
        }
        Reservation reservation = reservationOpt.get();
        if (!reservation.getUser().getId().equals(principal.getUser().getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "他人の予約はキャンセルできません。");
            return "redirect:/reservations";
        }
        reservationService.deleteReservation(reservation);
        redirectAttributes.addFlashAttribute("successMessage", "予約をキャンセルしました。");
        return "redirect:/reservations";
    }
}
