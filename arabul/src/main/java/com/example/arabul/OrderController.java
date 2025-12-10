package com.example.arabul;

import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> Order(@NotBlank(message = "Credit card number cannot be empty") @Size(min = 16, max = 16, message = "Credit cart number should be 16 characters") @RequestParam String creditCardNumber,
                                   @NotNull(message = "Verification code cannot be null") @Min(value = 0, message = "Verification code must be 3 digits") @Max(value = 999, message = "Verification code must be 3 digits") @RequestParam Integer verificationCode,
                                   @NotBlank(message = "Verification code cannot be empty") @RequestParam String expirationDate,
                                   @NotBlank(message = "First name cannot be empty") @RequestParam String firstName,
                                   @NotBlank(message = "Last name cannot be empty") @RequestParam String lastName,
                                   @NotEmpty(message = "Products is empty") @RequestBody List<Map<String, Object>> products) {

        Integer userId = RequestContext.getUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("info", "Unauthorized"));
        }

        if (!creditCardNumber.startsWith("5") && !creditCardNumber.startsWith("4")) {
            return ResponseEntity.status(400).body(Map.of("info", "Bad request"));
        }

        if (!expirationDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            return ResponseEntity.status(400).body(Map.of("info", "Bad request"));
        }


        try {
            repository.save(userId, creditCardNumber, verificationCode, expirationDate, firstName, lastName, products);
            return ResponseEntity.status(200).body(Map.of("info", "Ordered"));
        } catch (
                Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("info", "DB error"));
        }
    }

    @GetMapping
    public ResponseEntity<?> GetOrderInfos() {

        Integer userId = RequestContext.getUserId();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("info", "Unauthorized"));
        }

        List<Map<String, Object>> products = repository.getOrders(userId);

        return ResponseEntity.ok(products);

    }
}
