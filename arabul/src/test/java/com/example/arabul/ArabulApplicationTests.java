package com.example.arabul;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

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
                "photo",
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

    @Test
    void testAllProductsOk() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response);
        JsonNode firstProduct = root.get(0);

        String name = firstProduct.get("NAME").asText();
        assertEquals("hikayeler", name);
    }

    @Test
    @Transactional
    void testDeleteProductsOk() throws Exception {
        var mvcResult = mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testByIdProductsOk() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response);
        System.out.println(response);

        String name = root.get("NAME").asText();
        assertEquals("hikayeler", name);
    }
}
