package com.example.nagoyameshi.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.service.AdminRestaurantService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者用の店舗管理コントローラ。
 * 一覧表示・登録・更新・削除を提供する。
 */
@Controller
@RequiredArgsConstructor
public class AdminRestaurantController {

    /** 店舗情報を扱うサービス */
    private final AdminRestaurantService adminRestaurantService;

    /**
     * 店舗一覧ページを表示する。
     *
     * @param keyword 検索キーワード
     * @param page    ページ番号（0開始）
     * @param model   ビューへ渡すモデル
     * @return 一覧画面テンプレート名
     */
    @GetMapping("/admin/restaurants")
    public String index(@PageableDefault(page = 0, size = 15, sort = "id", direction = Direction.ASC) Pageable pageable,
                        @RequestParam(name = "keyword", required = false) String keyword,
                        Model model) {
        // 検索キーワードの有無で取得処理を分岐する
        Page<Restaurant> restaurantPage;
        if (keyword != null && !keyword.isBlank()) {
            restaurantPage = adminRestaurantService.getRestaurants(keyword, pageable);
        } else {
            restaurantPage = adminRestaurantService.getRestaurants(null, pageable);
        }
        // 取得した店舗情報と検索キーワードをビューへ渡す
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        return "admin/restaurants/index";
    }

    /**
     * 店舗登録フォームを表示する。
     */
    @GetMapping("/admin/restaurants/register")
    public String register(Model model) {
        model.addAttribute("restaurantRegisterForm", new RestaurantRegisterForm());
        return "admin/restaurants/register";
    }

    /**
     * 店舗を新規登録する。
     */
    @PostMapping("/admin/restaurants/create")
    public String create(@Validated RestaurantRegisterForm form, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        // 価格帯の妥当性チェック
        if (!adminRestaurantService.isValidPrices(form.getLowestPrice(), form.getHighestPrice())) {
            // rejectメソッドで独自エラーを追加
            bindingResult.reject("priceMismatch", "最高価格は最低価格以上に設定してください。");
        }
        // 営業時間の妥当性チェック
        if (!adminRestaurantService.isValidBusinessHours(form.getOpeningTime(), form.getClosingTime())) {
            bindingResult.reject("businessHoursMismatch", "閉店時間は開店時間より後に設定してください。");
        }

        // 入力エラーがある場合は登録画面を再表示
        if (bindingResult.hasErrors()) {
            return "admin/restaurants/register";
        }

        // 問題がなければ店舗を登録
        adminRestaurantService.createRestaurant(form);
        redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");
        return "redirect:/admin/restaurants";
    }

    /**
     * 店舗詳細を表示する。
     */
    @GetMapping("/admin/restaurants/{id}")
    public String show(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // 指定IDの店舗情報を取得
            Restaurant restaurant = adminRestaurantService.getRestaurant(id);
            model.addAttribute("restaurant", restaurant);
            return "admin/restaurants/show";
        } catch (IllegalArgumentException e) {
            // 存在しない場合は一覧へリダイレクト
            return "redirect:/admin/restaurants";
        }
    }

    /**
     * 店舗編集フォームを表示する。
     */
    @GetMapping("/admin/restaurants/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Restaurant restaurant = adminRestaurantService.getRestaurant(id);
            // 取得した情報をフォームに詰め替える
            RestaurantEditForm form = new RestaurantEditForm();
            form.setName(restaurant.getName());
            form.setDescription(restaurant.getDescription());
            form.setLowestPrice(restaurant.getLowestPrice());
            form.setHighestPrice(restaurant.getHighestPrice());
            form.setPostalCode(restaurant.getPostalCode());
            form.setAddress(restaurant.getAddress());
            form.setOpeningTime(restaurant.getOpeningTime());
            form.setClosingTime(restaurant.getClosingTime());
            form.setSeatingCapacity(restaurant.getSeatingCapacity());
            model.addAttribute("restaurantEditForm", form);
            model.addAttribute("restaurant", restaurant);
            return "admin/restaurants/edit";
        } catch (IllegalArgumentException e) {
            // 店舗が存在しない場合は一覧へリダイレクトしメッセージを表示
            redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません。");
            return "redirect:/admin/restaurants";
        }
    }

    /**
     * 店舗情報を更新する。
     */
    @PostMapping("/admin/restaurants/{id}/update")
    public String update(@PathVariable("id") Long id, @Validated RestaurantEditForm form,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurant", adminRestaurantService.getRestaurant(id));
            return "admin/restaurants/edit";
        }
        adminRestaurantService.update(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を更新しました。");
        return "redirect:/admin/restaurants/" + id;
    }

    /**
     * 店舗を削除する。
     */
    @PostMapping("/admin/restaurants/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        adminRestaurantService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        return "redirect:/admin/restaurants";
    }
}
