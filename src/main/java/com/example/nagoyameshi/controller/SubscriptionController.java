package com.example.nagoyameshi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 有料プラン登録や解約を行うコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final StripeService stripeService;
    private final UserService userService;

    /** 料金プランの価格ID */
    @Value("${stripe.premium-plan-price-id}")
    private String priceId;

    /**
     * 有料プラン登録ページを表示する。
     */
    @GetMapping("/subscription/register")
    public String register() {
        return "subscription/register";
    }

    /**
     * 有料プランへ登録する処理。
     */
    @PostMapping("/subscription/create")
    public String create(String paymentMethodId, RedirectAttributes redirectAttributes) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByEmail(email);

            // 顧客未登録の場合はStripe上に作成
            String customerId = user.getStripeCustomerId();
            if (customerId == null) {
                var customer = stripeService.createCustomer(user);
                customerId = customer.getId();
                userService.saveStripeCustomerId(user, customerId);
            }

            // 支払い方法を紐付けてデフォルトに設定
            stripeService.attachPaymentMethodToCustomer(paymentMethodId, customerId);
            stripeService.setDefaultPaymentMethod(paymentMethodId, customerId);

            // サブスクリプション作成
            stripeService.createSubscription(customerId, priceId);

            // ユーザーを有料会員に変更
            userService.updateRole(user, "ROLE_PAID_MEMBER");
            userService.refreshAuthenticationByRole("ROLE_PAID_MEMBER");

            redirectAttributes.addFlashAttribute("successMessage", "有料プランを登録しました。\n");
            return "redirect:/user";
        } catch (StripeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "決済処理でエラーが発生しました。\n");
            return "redirect:/";
        }
    }

    /**
     * お支払い方法編集ページを表示する。
     */
    @GetMapping("/subscription/edit")
    public String edit(Model model, RedirectAttributes redirectAttributes) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByEmail(email);
            PaymentMethod pm = stripeService.getDefaultPaymentMethod(user.getStripeCustomerId());
            model.addAttribute("card", pm.getCard());
            model.addAttribute("cardHolderName", pm.getBillingDetails().getName());
            return "subscription/edit";
        } catch (StripeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "情報取得に失敗しました。\n");
            return "redirect:/";
        }
    }

    /**
     * 支払い方法を更新する。
     */
    @PostMapping("/subscription/update")
    public String update(String paymentMethodId, RedirectAttributes redirectAttributes) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByEmail(email);
            String customerId = user.getStripeCustomerId();

            String oldPaymentMethodId = stripeService.getDefaultPaymentMethodId(customerId);
            if (oldPaymentMethodId != null) {
                stripeService.detachPaymentMethodFromCustomer(oldPaymentMethodId);
            }

            stripeService.attachPaymentMethodToCustomer(paymentMethodId, customerId);
            stripeService.setDefaultPaymentMethod(paymentMethodId, customerId);
            redirectAttributes.addFlashAttribute("successMessage", "お支払い方法を更新しました。\n");
            return "redirect:/user";
        } catch (StripeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "決済処理でエラーが発生しました。\n");
            return "redirect:/";
        }
    }

    /**
     * 解約確認ページを表示する。
     */
    @GetMapping("/subscription/cancel")
    public String cancel() {
        return "subscription/cancel";
    }

    /**
     * サブスクリプションを解約する処理。
     */
    @PostMapping("/subscription/delete")
    public String delete(RedirectAttributes redirectAttributes) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByEmail(email);
            String customerId = user.getStripeCustomerId();

            // 登録中のサブスクリプションを取得してキャンセル
            List<Subscription> subs = stripeService.getSubscriptions(customerId);
            stripeService.cancelSubscriptions(subs);

            // 支払い方法も解除
            String pmId = stripeService.getDefaultPaymentMethodId(customerId);
            if (pmId != null) {
                stripeService.detachPaymentMethodFromCustomer(pmId);
            }

            // 無料会員へ戻す
            userService.updateRole(user, "ROLE_FREE_MEMBER");
            userService.refreshAuthenticationByRole("ROLE_FREE_MEMBER");

            redirectAttributes.addFlashAttribute("successMessage", "有料プランを解約しました。\n");
            return "redirect:/user";
        } catch (StripeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "解約処理でエラーが発生しました。\n");
            return "redirect:/";
        }
    }
}
