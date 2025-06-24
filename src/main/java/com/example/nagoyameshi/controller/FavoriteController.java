package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.RestaurantService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 会員向けお気に入り機能を提供するコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final RestaurantService restaurantService;

    /** お気に入り一覧を表示する。 */
    @GetMapping("/favorites")
    public String index(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
            Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            return "redirect:/login"; // 未ログイン
        }
        String role = principal.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role) && !"ROLE_FREE_MEMBER".equals(role)) {
            return "redirect:/"; // 管理者などはアクセス不可
        }
        if ("ROLE_FREE_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "お気に入り機能の利用には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        User user = principal.getUser();
        Page<Favorite> favoritePage = favoriteService.findFavoritesByUserOrderByCreatedAtDesc(user, pageable);
        model.addAttribute("favoritePage", favoritePage);
        return "favorites/index";
    }

    /** お気に入りを追加する。 */
    @PostMapping("/restaurants/{restaurantId}/favorites/create")
    public String create(@PathVariable Long restaurantId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal)) {
            return "redirect:/login";
        }
        String role = principal.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role) && !"ROLE_FREE_MEMBER".equals(role)) {
            return "redirect:/";
        }
        if ("ROLE_FREE_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "お気に入り登録には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        var restaurantOpt = restaurantService.findRestaurantById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません。");
            return "redirect:/restaurants";
        }
        Restaurant restaurant = restaurantOpt.get();
        User user = principal.getUser();
        favoriteService.createFavorite(restaurant, user);
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
        return "redirect:/restaurants/" + restaurantId;
    }

    /** お気に入りを解除する。 */
    @PostMapping("/favorites/{favoriteId}/delete")
    public String delete(@PathVariable Long favoriteId, HttpServletRequest request,
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
            redirectAttributes.addFlashAttribute("errorMessage", "お気に入り解除には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        var favoriteOpt = favoriteService.findFavoriteById(favoriteId);
        if (favoriteOpt.isEmpty() || !favoriteOpt.get().getUser().getId().equals(principal.getUser().getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "お気に入り情報が取得できませんでした。");
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/favorites");
        }
        Favorite favorite = favoriteOpt.get();
        favoriteService.deleteFavorite(favorite);
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/favorites");
    }
}
