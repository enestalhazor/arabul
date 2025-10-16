package com.example.arabul;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepository repository;

    public CartController(CartRepository repository) {
        this.repository = repository;
    }


    public static class CartRequest {
        @JsonProperty("product_id")
        private Integer productId;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(int i) {
            productId = i;
        }
    }

    @PostMapping
    public ResponseEntity<?> UpdateCart(@RequestBody CartController.CartRequest request) throws IOException {

        Integer productId = request.getProductId();
        Integer userId = RequestContext.getUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("info", "Unauthorized"));
        }

        if (productId == null) {
            return ResponseEntity.status(400).body(Map.of("info", "Bad request"));
        }

        if (!repository.isProductExists(productId)) {
            return ResponseEntity.status(404).body(Map.of("info", "Product not found"));
        }

        if (repository.isProductLimitExceeded(productId)) {
            return ResponseEntity.status(422).body(Map.of("info", "Product limit exceeded"));
        }

        try {
            repository.save(productId, userId);
            return ResponseEntity.ok(Map.of("info", "Cart updated."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("info", "DB Error"));
        }
    }

    @GetMapping
    public ResponseEntity<?> GetCart() throws IOException {

        Integer userId = RequestContext.getUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("info", "Unauthorized"));
        }

        List<Map<String, Object>> cart = repository.getCart(userId);

        if (cart == null) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(cart);
    }

    @Validated
    @DeleteMapping("/{product_id}")
    public ResponseEntity<?> DeleteProductByCart(@PathVariable Integer product_id) throws IOException {

        Integer userId = RequestContext.getUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("info", "Unauthorized"));
        }

        try {
            repository.deleteProductByCart(product_id, userId);
            return ResponseEntity.ok(Map.of("info", "Product deleted"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("info", "DB error"));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> ClearCart() throws IOException {

        Integer userId = RequestContext.getUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("info", "Unauthorized"));
        }

        try {
            repository.clearCart(userId);
            return ResponseEntity.ok(Map.of("info", "Cleared cart"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("info", "DB error"));
        }
    }
}
