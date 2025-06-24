package com.example.nagoyameshi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// Spring Framework が提供する MockitoBean を使用
// MockBean は 3.4.0 以降非推奨となったため置き換える
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.event.SignupEventPublisher;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // UserService をモック化してテスト用のコンテキストに登録する
    @MockitoBean
    private UserService userService;
    
    // SignupEventPublisher もモック化して登録する
    @MockitoBean
    private SignupEventPublisher signupEventPublisher;


    @Test
    @DisplayName("POST /register はステータス200を返す")
    void registerメソッドはステータス200を返す() throws Exception {
        String json = "{\"email\":\"a@example.com\",\"password\":\"pass\"}";
        mockMvc.perform(post("/register").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
}
