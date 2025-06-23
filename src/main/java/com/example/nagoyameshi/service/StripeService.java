package com.example.nagoyameshi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerRetrieveParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodDetachParams;
import com.stripe.param.SubscriptionCreateParams;

import jakarta.annotation.PostConstruct;

/**
 * Stripe API を利用した課金関連の処理を提供するサービスクラスです。
 */
@Service
public class StripeService {

    /** Stripe の秘密鍵 */
    @Value("${stripe.api-key}")
    private String apiKey;

    /**
     * サービス初期化時に API キーを設定する。
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    /**
     * 新しい顧客を作成する。
     *
     * @param user アプリケーションのユーザー
     * @return 作成された顧客オブジェクト
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public Customer createCustomer(User user) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();
        return Customer.create(params);
    }

    /**
     * 支払い方法を顧客に紐付ける。
     *
     * @param paymentMethodId 支払い方法ID
     * @param customerId      顧客ID
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public void attachPaymentMethodToCustomer(String paymentMethodId, String customerId)
            throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();
        paymentMethod.attach(params);
    }

    /**
     * 顧客のデフォルト支払い方法を設定する。
     *
     * @param paymentMethodId 支払い方法ID
     * @param customerId      顧客ID
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public void setDefaultPaymentMethod(String paymentMethodId, String customerId)
            throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        CustomerUpdateParams params = CustomerUpdateParams.builder()
                .setInvoiceSettings(
                        CustomerUpdateParams.InvoiceSettings.builder()
                                .setDefaultPaymentMethod(paymentMethodId)
                                .build())
                .build();
        customer.update(params);
    }

    /**
     * サブスクリプションを作成する。
     *
     * @param customerId 顧客ID
     * @param priceId    価格ID
     * @return 作成されたサブスクリプション
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public Subscription createSubscription(String customerId, String priceId) throws StripeException {
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(
                        SubscriptionCreateParams.Item.builder()
                                .setPrice(priceId)
                                .build())
                .build();
        return Subscription.create(params);
    }

    /**
     * 顧客のデフォルト支払い方法を取得する。
     *
     * @param customerId 顧客ID
     * @return デフォルトの支払い方法
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public PaymentMethod getDefaultPaymentMethod(String customerId) throws StripeException {
        CustomerRetrieveParams params = CustomerRetrieveParams.builder()
                .addExpand("invoice_settings.default_payment_method")
                .build();
        Customer customer = Customer.retrieve(customerId, params, null);
        return customer.getInvoiceSettings().getDefaultPaymentMethodObject();
    }

    /**
     * デフォルト支払い方法のIDを取得する。
     *
     * @param customerId 顧客ID
     * @return 支払い方法ID（存在しない場合は null）
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public String getDefaultPaymentMethodId(String customerId) throws StripeException {
        PaymentMethod pm = getDefaultPaymentMethod(customerId);
        return pm == null ? null : pm.getId();
    }

    /**
     * 顧客から支払い方法を切り離す。
     *
     * @param paymentMethodId 支払い方法ID
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public void detachPaymentMethodFromCustomer(String paymentMethodId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        PaymentMethodDetachParams params = PaymentMethodDetachParams.builder().build();
        paymentMethod.detach(params);
    }

    /**
     * 顧客に紐付くサブスクリプション一覧を取得する。
     *
     * @param customerId 顧客ID
     * @return サブスクリプションのリスト
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public List<Subscription> getSubscriptions(String customerId) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        return Subscription.list(params).getData();
    }

    /**
     * 与えられたサブスクリプションをすべてキャンセルする。
     *
     * @param subscriptions キャンセル対象のサブスクリプション
     * @throws StripeException Stripe API でエラーが発生した場合
     */
    public void cancelSubscriptions(List<Subscription> subscriptions) throws StripeException {
        for (Subscription sub : subscriptions) {
            sub.cancel();
        }
    }
}
