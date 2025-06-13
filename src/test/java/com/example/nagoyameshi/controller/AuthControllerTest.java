package com.example.nagoyameshi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.service.UserService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST /register returns 200")
    void testRegister() throws Exception {
        String json = "{\"email\":\"a@example.com\",\"password\":\"pass\"}";
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
}
