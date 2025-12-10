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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    ProductRepository repository = mock(ProductRepository.class);

    //* test repository.save should be called with ("masallar", "masal", "masal.jpeg", "196.50", "book")
    //* and make sure photo is uploaded
    //* mock file upload and if transferTo is worked.
    @Test
    @Transactional
    void testCreateProductOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "photo",
                "masal.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                .file(file)
                .param("name", "masallar")
                .param("description", "masal")
                .param("price", "196.50")
                .param("category", "book"));
    }

    // missing name
    @Test
    @Transactional
    void testCreateProductMissingRequiredField1() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "photo",
                "masal.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .file(file)
                        .param("description", "masal")
                        .param("price", "196.50")
                        .param("category", "book"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));

    }

    // missing description
    @Test
    @Transactional
    void testCreateProductMissingRequiredField2() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "photo",
                "masal.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .file(file)
                        .param("name", "masallar")
                        .param("price", "196.50")
                        .param("category", "book"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));

    }

    // missing price
    @Test
    @Transactional
    void testCreateProductMissingRequiredField3() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "photo",
                "masal.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .file(file)
                        .param("name", "masallar")
                        .param("description", "masal")
                        .param("category", "book"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));

    }

    // missing category
    @Test
    @Transactional
    void testCreateProductMissingRequiredField4() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "photo",
                "masal.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .file(file)
                        .param("name", "masallar")
                        .param("description", "masal")
                        .param("price", "196.50"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));

    }

    @Test
    @Transactional
    void testAllProductsOk() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response);

        assertEquals(2, root.size());  // Example: expect 3 products
        assertEquals("hikayeler", root.get(0).get("NAME").asText());
        assertEquals("masallar", root.get(1).get("NAME").asText());
    }

    @Test
    @Transactional
    @Sql(statements = "DELETE FROM products")
    void testAllProductsOk1() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response);

        assertEquals(0, root.size());
    }

    @Test
    @Transactional
    void testDeleteProductsOk() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Product deleted"));

    }

    @Test
    @Transactional
    void testDeleteProductsOk1() throws Exception {
        mockMvc.perform(delete("/api/products/3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("Not found"));
    }

    @Test
    @Transactional
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

    @Test
    @Transactional
    void testByIdProductsNotFound() throws Exception {
        mockMvc.perform(get("/api/products/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void testByTermProductsOk() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/products/search?term=hikayeler"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response);
        JsonNode firstProduct = root.get(0);
        System.out.println(response);

        String name = firstProduct.get("NAME").asText();
        assertEquals("hikayeler", name);
    }

    @Test
    @Transactional
    void testByTermProductsNotFound() throws Exception {

        mockMvc.perform(get("/api/products/search?term=tarraklar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("No products found for term: tarraklar"));
    }
}
