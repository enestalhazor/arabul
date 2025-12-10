package com.example.arabul;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository repository;

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
    void testOrder_Unauthorized() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(null);

        mockMvc.perform(post("/api/order")
                        .param("creditCardNumber", "5123456789012345")
                        .param("verificationCode", "123")
                        .param("expirationDate", "12/25")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .content("[{\"product_id\":1,\"count\":2}]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));

        verify(repository, never()).save(any(), any(), any(), any(), any(), any(), anyList());
    }

    @Test
    @Transactional
    void testOrder_EmptyCart() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(1);

        mockMvc.perform(post("/api/order")
                        .param("creditCardNumber", "5123456789012345")
                        .param("verificationCode", "123")
                        .param("expirationDate", "12/25")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .content("[]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(repository, never()).save(any(), any(), any(), any(), any(), any(), anyList());
    }

    @Test
    @Transactional
    void testOrder_InvalidCreditCard() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(1);

        mockMvc.perform(post("/api/order")
                        .param("creditCardNumber", "6123456789012345") // invalid start
                        .param("verificationCode", "123")
                        .param("expirationDate", "12/25")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .content("[{\"product_id\":1,\"count\":2}]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Bad request"));

        verify(repository, never()).save(any(), any(), any(), any(), any(), any(), anyList());
    }


    @Test
    @Transactional
    void testOrder_InvalidExpirationDate() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(1);

        mockMvc.perform(post("/api/order")
                        .param("creditCardNumber", "5123456789012345")
                        .param("verificationCode", "123")
                        .param("expirationDate", "2025-12") // invalid format
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .content("[{\"product_id\":1,\"count\":2}]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Bad request"));

        verify(repository, never()).save(any(), any(), any(), any(), any(), any(), anyList());
    }


    @Test
    @Transactional
    void testOrder_Success() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(1);
        when(repository.save(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyList()))
                .thenReturn(123); // mock order ID

        mockMvc.perform(post("/api/order")
                        .param("creditCardNumber", "5123456789012345")
                        .param("verificationCode", "123")
                        .param("expirationDate", "12/25")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .content("[{\"product_id\":1,\"count\":2}]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Ordered"));

        verify(repository, times(1)).save(any(), any(), any(), any(), any(), any(), anyList());
    }

    // GET
    @Test
    @Transactional
    void testGetOrders_Unauthorized() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(null);

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));

        verify(repository, never()).getOrders(1);
    }

    @Test
    @Transactional
    void testGetOrders_Empty() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(1);

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(repository, times(1)).getOrders(1);
    }

    @Test
    @Transactional
    void testGetOrders_WithItems() throws Exception {
        mockedRequestContext.when(RequestContext::getUserId).thenReturn(1);

        List<Map<String, Object>> mockProducts = List.of(
                Map.of("product_id", 10, "count", 2),
                Map.of("product_id", 11, "count", 1)
        );

        // Suppose getProducts now returns a list of orders
        List<Map<String, Object>> mockOrders = List.of(
                Map.of(
                        "order_id", 12,
                        "credit_card_number", "5555555555555554",
                        "verification_code", 554,
                        "expiration_date", "12/28",
                        "first_name", "Enes",
                        "last_name", "Zor",
                        "products", mockProducts
                ),
                Map.of(
                        "order_id", 13,
                        "credit_card_number", "5555555555555552",
                        "verification_code", 552,
                        "expiration_date", "12/25",
                        "first_name", "Hamo",
                        "last_name", "Zor",
                        "products", List.of(Map.of("product_id", 10, "count", 3), Map.of("product_id", 11, "count", 6), Map.of("product_id", 12, "count", 1))
                )
        );

        when(repository.getOrders(1)).thenReturn(mockOrders);

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                // Access the first order in the list
                .andExpect(jsonPath("$[0].order_id").value(12))
                .andExpect(jsonPath("$[0].credit_card_number").value("5555555555555554"))
                .andExpect(jsonPath("$[0].verification_code").value(554))
                .andExpect(jsonPath("$[0].expiration_date").value("12/28"))
                .andExpect(jsonPath("$[0].first_name").value("Enes"))
                .andExpect(jsonPath("$[0].last_name").value("Zor"))
                .andExpect(jsonPath("$[0].products").isArray())
                .andExpect(jsonPath("$[0].products[0].product_id").value(10))
                .andExpect(jsonPath("$[0].products[0].count").value(2))
                .andExpect(jsonPath("$[0].products[1].product_id").value(11))
                .andExpect(jsonPath("$[0].products[1].count").value(1))
                .andExpect(jsonPath("$[1].order_id").value(13))
                .andExpect(jsonPath("$[1].credit_card_number").value("5555555555555552"))
                .andExpect(jsonPath("$[1].verification_code").value(552))
                .andExpect(jsonPath("$[1].expiration_date").value("12/25"))
                .andExpect(jsonPath("$[1].first_name").value("Hamo"))
                .andExpect(jsonPath("$[1].last_name").value("Zor"))
                .andExpect(jsonPath("$[1].products").isArray())
                .andExpect(jsonPath("$[1].products[0].product_id").value(10))
                .andExpect(jsonPath("$[1].products[0].count").value(3))
                .andExpect(jsonPath("$[1].products[1].product_id").value(11))
                .andExpect(jsonPath("$[1].products[1].count").value(6))
                .andExpect(jsonPath("$[1].products[2].product_id").value(12))
                .andExpect(jsonPath("$[1].products[2].count").value(1));

        verify(repository, times(1)).getOrders(1);
    }
}
