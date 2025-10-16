package com.example.arabul;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartRepository repository;

    private MockedStatic<RequestContext> mockedRequestContext;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockedRequestContext = mockStatic(RequestContext.class);
    }

    @AfterEach
    void teardown() {
        mockedRequestContext.close();
    }

    // POST
    @Test
    @Transactional
    void testAllProductsOk() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);
        when(repository.getCart(10)).thenReturn(null);

        var mvcResult = mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();

        verify(repository, times(1)).getCart(10);
    }

    @Test
    @Transactional
    void testUpdateCartOk() throws Exception {
        var requestJson = "{\"product_id\":1}";
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);
        when(repository.isProductExists(1)).thenReturn(true);
        when(repository.isProductLimitExceeded(1)).thenReturn(false);

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Cart updated."));

        verify(repository, times(1)).save(1, 10);
    }

    @Test
    @Transactional
    void testUpdateCartNoToken() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(null);

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));
    }

    @Test
    @Transactional
    void testUpdateCartMissingProductId() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Bad request"));
    }

    @Test
    @Transactional
    void testUpdateCartProductNotFound() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);
        when(repository.isProductExists(999)).thenReturn(false);

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"product_id\":999}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("Product not found"));
    }

    @Test
    @Transactional
    void testUpdateCartLimitExceeded() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);
        when(repository.isProductExists(2)).thenReturn(true);
        when(repository.isProductLimitExceeded(2)).thenReturn(true);

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"product_id\":2}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.info").value("Product limit exceeded"));
    }

    //GET CART
    @Test
    @Transactional
    void testGetCartEmpty() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);
        when(repository.getCart(10)).thenReturn(null);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @Transactional
    void testGetCartUnauthorized() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(null);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"))
                .andReturn();

        verify(repository, times(0)).getCart(any());
    }

    @Test
    @Transactional
    void testGetCartOk() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);
        List<Map<String, Object>> fakeCart = List.of(
                Map.of("productId", 1),
                Map.of("productId", 2)
        );

        when(repository.getCart(10)).thenReturn(fakeCart);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[1].productId").value(2));

        verify(repository, times(1)).getCart(10);
    }

    //DELETE PRODUCT
    @Test
    @Transactional
    void testDeleteProductOk() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);

        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Product deleted"));

        verify(repository, times(1)).deleteProductByCart(1, 10);
    }

    @Test
    @Transactional
    void testDeleteProductUnauthorized() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(null);

        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));

        verify(repository, never()).deleteProductByCart(anyInt(), anyInt());
    }
    
    //CLEAR CART
    @Test
    @Transactional
    void testClearCartOk() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(10);

        mockMvc.perform(delete("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Cleared cart"));

        verify(repository, times(1)).clearCart(10);
    }

    @Test
    @Transactional
    void testClearCartUnauthorized() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(null);

        mockMvc.perform(delete("/api/cart"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));

        verify(repository, never()).clearCart(any());
    }
}