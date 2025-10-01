package com.example.arabul;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class ArabulApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepoProcess repository;

    @Test
    void testCreateProductOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "profilepicture",
                "masal.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                .file(file)
                .param("name", "masallar")
                .param("description", "masal")
                .param("price", "196.50")
                .param("category", "book"));

        resultActions.andExpect(status().isOk());
    }

}
