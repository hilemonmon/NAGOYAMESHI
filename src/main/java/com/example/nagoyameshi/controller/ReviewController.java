package com.example.nagoyameshi.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;

import lombok.RequiredArgsConstructor;

/**
 * 会員向けレビュー機能を提供するコントローラ。
 */
@Controller
@RequestMapping("/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;

    /** レビュー一覧を表示する。 */
    @GetMapping
    public String index(@PathVariable Long restaurantId,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.DESC) Pageable pageable,
            Model model) {
        var restaurantOpt = restaurantService.findRestaurantById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return "redirect:/restaurants";
        }
        Restaurant restaurant = restaurantOpt.get();

        Page<Review> reviewPage = reviewService.findReviewsByRestaurantOrderByCreatedAtDesc(restaurantId, pageable);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        Long userId = null;
        if (auth.getPrincipal() instanceof UserDetailsImpl principal) {
            userId = principal.getUser().getId();
        }
        boolean hasReviewed = userId != null && reviewService.hasUserAlreadyReviewed(restaurantId, userId);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("userRoleName", role);
        model.addAttribute("hasUserAlreadyReviewed", hasReviewed);
        return "reviews/index";
    }

    /** 投稿ページを表示する。無料会員は有料登録ページへ誘導。 */
    @GetMapping("/register")
    public String register(@PathVariable Long restaurantId, ReviewRegisterForm form,
            RedirectAttributes redirectAttributes, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビュー投稿には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        return restaurantService.findRestaurantById(restaurantId)
                .map(r -> {
                    model.addAttribute("restaurant", r);
                    return "reviews/register";
                }).orElse("redirect:/restaurants");
    }

    /** 新規レビューを作成する。 */
    @PostMapping("/create")
    public String create(@PathVariable Long restaurantId, @Validated ReviewRegisterForm form, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        if (!"ROLE_PAID_MEMBER".equals(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビュー投稿には有料プラン登録が必要です。");
            return "redirect:/subscription/register";
        }
        var restaurantOpt = restaurantService.findRestaurantById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return "redirect:/restaurants";
        }
        Restaurant restaurant = restaurantOpt.get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurant", restaurant);
            return "reviews/register";
        }

        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();
        reviewService.createReview(form, restaurant, user);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
        return "redirect:/restaurants/" + restaurantId + "/reviews";
    }

    /** 編集ページを表示する。 */
    @GetMapping("/{reviewId}/edit")
    public String edit(@PathVariable Long restaurantId, @PathVariable Long reviewId, Model model,
            RedirectAttributes redirectAttributes) {
        var reviewOpt = reviewService.findReviewById(reviewId);
        if (reviewOpt.isEmpty() || !reviewOpt.get().getRestaurant().getId().equals(restaurantId)) {
            return "redirect:/restaurants/" + restaurantId;
        }
        Review review = reviewOpt.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal) || !principal.getUser().getId().equals(review.getUser().getId())) {
            return "redirect:/restaurants/" + restaurantId;
        }
        ReviewEditForm form = new ReviewEditForm(review.getScore(), review.getContent());
        model.addAttribute("reviewEditForm", form);
        model.addAttribute("restaurant", review.getRestaurant());
        model.addAttribute("review", review);
        return "reviews/edit";
    }

    /** レビューを更新する。 */
    @PostMapping("/{reviewId}/update")
    public String update(@PathVariable Long restaurantId, @PathVariable Long reviewId,
            @Validated ReviewEditForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes,
            Model model) {
        var reviewOpt = reviewService.findReviewById(reviewId);
        if (reviewOpt.isEmpty() || !reviewOpt.get().getRestaurant().getId().equals(restaurantId)) {
            return "redirect:/restaurants/" + restaurantId;
        }
        Review review = reviewOpt.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal) || !principal.getUser().getId().equals(review.getUser().getId())) {
            return "redirect:/restaurants/" + restaurantId;
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurant", review.getRestaurant());
            model.addAttribute("review", review);
            return "reviews/edit";
        }
        reviewService.updateReview(review, form);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを更新しました。");
        return "redirect:/restaurants/" + restaurantId + "/reviews";
    }

    /** レビューを削除する。 */
    @PostMapping("/{reviewId}/delete")
    public String delete(@PathVariable Long restaurantId, @PathVariable Long reviewId,
            RedirectAttributes redirectAttributes) {
        var reviewOpt = reviewService.findReviewById(reviewId);
        if (reviewOpt.isEmpty() || !reviewOpt.get().getRestaurant().getId().equals(restaurantId)) {
            return "redirect:/restaurants/" + restaurantId;
        }
        Review review = reviewOpt.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl principal) || !principal.getUser().getId().equals(review.getUser().getId())) {
            return "redirect:/restaurants/" + restaurantId;
        }
        reviewService.deleteReview(review);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
        return "redirect:/restaurants/" + restaurantId + "/reviews";
    }
}
