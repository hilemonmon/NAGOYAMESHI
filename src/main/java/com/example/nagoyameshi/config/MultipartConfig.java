package com.example.nagoyameshi.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.MultipartFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

/**
 * multipart/form-data のリクエストをセキュリティフィルタより前に
 * 処理させるための設定クラスです。
 * これを行うことで、CSRF トークンが正常に検証されるようになります。
 */
@Configuration
public class MultipartConfig {

    /** Logger for this class */
    private static final Logger log = LoggerFactory.getLogger(MultipartConfig.class);

    /**
     * Called after bean creation to confirm that this configuration class
     * is loaded when the application starts.
     */
    @PostConstruct
    public void init() {
        // 出力内容は起動ログで確認可能
        log.info("MultipartConfig has been loaded");
    }

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
