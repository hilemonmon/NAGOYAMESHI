package com.example.nagoyameshi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.service.RestaurantService;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    @DisplayName("GET /restaurants returns restaurants")
    void testGetRestaurants() throws Exception {
        Restaurant r = Restaurant.builder().id(1L).name("Nagoya Ramen").description("desc").build();
        when(restaurantService.getRestaurants("Ramen")).thenReturn(List.of(r));

        mockMvc.perform(get("/restaurants").param("name", "Ramen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Nagoya Ramen"));
    }
}
