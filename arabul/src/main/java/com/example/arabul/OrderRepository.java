package com.example.arabul;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer save(Integer userId, String creditCardNumber, Integer verificationCode, String expirationDate, String firstName, String lastName, List<Map<String, Object>> products) {
        String sql = "INSERT INTO orders (user_id, credit_card_number, verification_code, expiration_date, first_name, last_name) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        Integer orderId = jdbcTemplate.queryForObject(sql, Integer.class, userId, creditCardNumber, verificationCode, expirationDate, firstName, lastName);

        String sql2 = "INSERT INTO order_products (order_id, product_id, count) VALUES (?, ?, ?)";

        for (Map<String, Object> item : products) {
            Long productId = ((Number) item.get("product_id")).longValue();
            Integer count = ((Number) item.get("count")).intValue();

            jdbcTemplate.update(sql2, orderId, productId, count);
        }

        return orderId;
    }

    public List<Map<String, Object>> getOrders(Integer userId) {

        // get orders by user id
        String sql = "SELECT * FROM orders WHERE user_id=?";
        List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, userId);

        // for get products
        String sql2 = "SELECT * FROM order_products WHERE order_id=?";

        // for product info
        String sql3 = "SELECT * FROM products WHERE id=?";

        Float totalPrice = 0f;

        // for returning response
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> order : orders) {
            Integer orderId = ((Number) order.get("id")).intValue();

            // put order infos
            Map<String, Object> orderMap = new LinkedHashMap<>();
            orderMap.put("order_id", orderId);
            orderMap.put("credit_card_number", order.get("credit_card_number"));
            orderMap.put("verification_code", order.get("verification_code"));
            orderMap.put("expiration_date", order.get("expiration_date"));
            orderMap.put("first_name", order.get("first_name"));
            orderMap.put("last_name", order.get("last_name"));

            // get products by order id
            List<Map<String, Object>> products = jdbcTemplate.queryForList(sql2, orderId);
            List<Map<String, Object>> productList = new ArrayList<>();
            for (Map<String, Object> product : products) {

                Integer productId = ((Number) product.get("product_id")).intValue();
                Map<String, Object> productInfos = jdbcTemplate.queryForMap(sql3, productId);

                Map<String, Object> productMap = new LinkedHashMap<>();
                productMap.put("product_id", product.get("product_id"));
                productMap.put("count", product.get("count"));

                productMap.put("name", productInfos.get("name"));
                productMap.put("description", productInfos.get("description"));
                productMap.put("photo", productInfos.get("photo"));
                productMap.put("price", productInfos.get("price"));
                productMap.put("category", productInfos.get("category"));

                productList.add(productMap);
            }
            orderMap.put("products", productList);
            result.add(orderMap);
        }

        return result;
    }
}
