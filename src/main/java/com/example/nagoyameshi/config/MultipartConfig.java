package com.example.nagoyameshi.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * multipart/form-data のリクエストをセキュリティフィルタより前に
 * 処理させるための設定クラスです。
 * これを行うことで、CSRF トークンが正常に検証されるようになります。
 */
@Configuration
public class MultipartConfig {

    /**
     * MultipartFilter を登録し、Spring Security より前に実行させます。
     * @return フィルタ登録ビーン
     */
    @Bean
    public FilterRegistrationBean<MultipartFilter> multipartFilter() {
        FilterRegistrationBean<MultipartFilter> registration = new FilterRegistrationBean<>(new MultipartFilter());
        // 先に実行されるよう優先度を高める
        registration.setOrder(0);
        return registration;
    }
}
