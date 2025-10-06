package com.example.arabul;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    UserRepository repository = mock(UserRepository.class);

    @Test
    void login_success_returns200AndToken() throws Exception {
        Mockito.when(repository.check(eq("enes@gmail.com"), anyString())).thenReturn(1);

        String json = "{ \"email\": \"enes@gmail.com\", \"password\": \"12345\" }";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", org.hamcrest.Matchers.startsWith("Bearer ")))
                .andExpect(jsonPath("$.token").exists());
    }

    // --- 2) Missing fields ---
    @Test
    void login_missingFields_returns400() throws Exception {
        String json = "{ \"email\": \"\", \"password\": \"\" }"; // fixed JSON

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing required fields"));
    }

    // --- 3) User not found ---
    @Test
    void login_userNotFound_returns404() throws Exception {
        Mockito.when(repository.check(eq("nouser@example.com"), anyString())).thenReturn(null);

        String json = "{ \"email\": \"nouser@example.com\", \"password\": \"wrongpass\" }";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}

