package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者ダッシュボードを表示するコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class AdminHomeController {

    /** ユーザー関連サービス */
    private final UserService userService;
    /** 店舗関連サービス */
    private final RestaurantService restaurantService;
    /** 予約関連サービス */
    private final ReservationService reservationService;

    /**
     * 管理者トップページを表示する。
     *
     * @param model ビューへ渡すモデル
     * @return テンプレート名
     */
    @GetMapping("/admin")
    public String index(Model model) {
        long free = userService.countUsersByRole_Name("ROLE_FREE_MEMBER");
        long paid = userService.countUsersByRole_Name("ROLE_PAID_MEMBER");
        long total = free + paid;
        long restaurants = restaurantService.countRestaurants();
        long reservations = reservationService.countReservations();
        long sales = paid * 300;

        model.addAttribute("totalFreeMembers", free);
        model.addAttribute("totalPaidMembers", paid);
        model.addAttribute("totalMembers", total);
        model.addAttribute("totalRestaurants", restaurants);
        model.addAttribute("totalReservations", reservations);
        model.addAttribute("salesForThisMonth", sales);

        return "admin/index";
    }
}
