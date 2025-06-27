package com.example.nagoyameshi.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * {@link MultipartConfig} がアプリケーション起動時に正しく読み込まれるかを検証するテスト。
 */
@SpringBootTest
class MultipartConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FilterRegistrationBean<MultipartFilter> multipartFilter;

    @Test
    @DisplayName("multipartFilter Bean がコンテキストに登録されている")
    void multipartFilter_Beanが登録されている() {
        // Bean 名を指定して存在するか確認
        assertThat(context.containsBean("multipartFilter")).isTrue();
        // Bean のインスタンスも null でないことを確認
        assertThat(multipartFilter).isNotNull();
    }
}
