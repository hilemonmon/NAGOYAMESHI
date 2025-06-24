package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.RestaurantService;

import lombok.RequiredArgsConstructor;

/**
 * 会員向けの店舗一覧を表示するコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class RestaurantController {
    /** 店舗情報を扱うサービス */
    private final RestaurantService restaurantService;
    /** カテゴリ情報を扱うサービス */
    private final CategoryService categoryService;

    /**
     * 店舗一覧ページを表示する。
     * 検索・並び替え・ページングに対応する。
     */
    @GetMapping("/restaurants")
    public String index(
            @PageableDefault(page = 0, size = 15, sort = "id", direction = Direction.ASC) Pageable pageable,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "price", required = false) Integer price,
            @RequestParam(name = "order", required = false) String order,
            Model model) {

        String sortOrder = (order == null) ? "createdAtDesc" : order;
        Page<Restaurant> restaurantPage;

        if (keyword != null && !keyword.isBlank()) {
            // キーワード検索
            if ("lowestPriceAsc".equals(sortOrder)) {
                restaurantPage = restaurantService
                        .findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(keyword, pageable);
            } else if ("ratingDesc".equals(sortOrder)) {
                restaurantPage = restaurantService
                        .findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(keyword, pageable);
            } else if ("popularDesc".equals(sortOrder)) {
                restaurantPage = restaurantService
                        .findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(keyword, pageable);
            } else {
                restaurantPage = restaurantService
                        .findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(keyword, pageable);
            }
        } else if (categoryId != null) {
            // カテゴリ検索
            if ("lowestPriceAsc".equals(sortOrder)) {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByLowestPriceAsc(categoryId, pageable);
            } else if ("ratingDesc".equals(sortOrder)) {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByAverageScoreDesc(categoryId, pageable);
            } else if ("popularDesc".equals(sortOrder)) {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByReservationCountDesc(categoryId, pageable);
            } else {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
            }
        } else if (price != null) {
            // 予算検索
            if ("lowestPriceAsc".equals(sortOrder)) {
                restaurantPage = restaurantService.findRestaurantsByLowestPriceLessThanEqualOrderByLowestPriceAsc(price,
                        pageable);
            } else if ("ratingDesc".equals(sortOrder)) {
                restaurantPage = restaurantService
                        .findRestaurantsByLowestPriceLessThanEqualOrderByAverageScoreDesc(price, pageable);
            } else if ("popularDesc".equals(sortOrder)) {
                restaurantPage = restaurantService
                        .findRestaurantsByLowestPriceLessThanEqualOrderByReservationCountDesc(price, pageable);
            } else {
                restaurantPage = restaurantService
                        .findRestaurantsByLowestPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            }
        } else {
            // 検索なしの場合
            if ("lowestPriceAsc".equals(sortOrder)) {
                restaurantPage = restaurantService.findAllRestaurantsByOrderByLowestPriceAsc(pageable);
            } else if ("ratingDesc".equals(sortOrder)) {
                restaurantPage = restaurantService.findAllRestaurantsByOrderByAverageScoreDesc(pageable);
            } else if ("popularDesc".equals(sortOrder)) {
                restaurantPage = restaurantService.findAllRestaurantsOrderByReservationCountDesc(pageable);
            } else {
                restaurantPage = restaurantService.findAllRestaurantsByOrderByCreatedAtDesc(pageable);
            }
        }

        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("price", price);
        model.addAttribute("order", sortOrder);
        return "restaurants/index";
    }

    /**
     * 店舗詳細ページを表示する。
     *
     * @param id   表示する店舗の ID
     * @param model モデルオブジェクト
     * @param redirectAttributes リダイレクト時に渡す属性
     * @return 詳細画面テンプレート名または一覧へのリダイレクト
     */
    @GetMapping("/restaurants/{id}")
    public String show(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        var restaurantOpt = restaurantService.findRestaurantById(id.longValue());
        if (restaurantOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません。");
            return "redirect:/restaurants";
        }
        model.addAttribute("restaurant", restaurantOpt.get());
        return "restaurants/show";
    }
}
