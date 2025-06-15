package com.example.nagoyameshi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// Spring Framework が提供する MockitoBean を使用
// MockBean は 3.4.0 以降非推奨となったため置き換える
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.service.UserService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // UserService をモック化してテスト用のコンテキストに登録する
    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("POST /register returns 200")
    void testRegister() throws Exception {
        String json = "{\"email\":\"a@example.com\",\"password\":\"pass\"}";
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
}
