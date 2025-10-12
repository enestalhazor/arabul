package com.example.arabul;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CartRepository {
    private JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isProductExists(Integer productId) {
        String sql = "SELECT COUNT(*) FROM products WHERE id=?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return result != null && result > 0;
    }

    public boolean isProductLimitExceeded(Integer productId) {
        String sql = "SELECT \"count\" FROM cart WHERE product_id=?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
            return count != null && count >= 10;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(Integer product_id, Integer user_id) {

        String sql1 = "SELECT COUNT(*) FROM cart WHERE product_id=? AND user_id=?";
        Integer count = jdbcTemplate.queryForObject(sql1, Integer.class, product_id, user_id);

        if (count != null && count > 0) {
            String updateCount = "UPDATE cart SET count = count + 1 WHERE product_id=? AND user_id=?";
            jdbcTemplate.update(updateCount, product_id, user_id);
            return;
        }

        String sql3 = "INSERT INTO cart(product_id, user_id, count) VALUES (?, ?, 1)";
                jdbcTemplate.update(sql3, product_id, user_id);
        return;
    }

    public List<Map<String, Object>> getCart(Integer userId) {
        String sql = "SELECT * FROM cart WHERE user_id=?";
        try {
            return jdbcTemplate.queryForList(sql, userId);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    public void deleteProductByCart(Integer productId, Integer userId) {

        String sql = "SELECT \"count\" FROM cart WHERE product_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);

        if (count == null) {
            return;
        }

        if (count > 1) {
            String updateCount = "UPDATE cart SET count = count - 1 WHERE product_id=? AND user_id=?";
            jdbcTemplate.update(updateCount, productId, userId);
            return;
        }
        String sql1 = "DELETE FROM cart WHERE product_id=?";
        jdbcTemplate.update(sql1, productId);
    }

    public void clearCart(Integer userId) {
        String sql = "DELETE FROM cart WHERE user_id=?";
        jdbcTemplate.update(sql, userId);
    }
}
