package com.example.arabul;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RepoProcess {

    public JdbcTemplate jdbcTemplate;

    public RepoProcess(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean save(String name, String description, String photo, float price, String category) {

        String sql = "INSERT INTO products(name, description, photo, price, category) VALUES (?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(sql, name, description, photo, price, category) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> products() {

        String sql = "SELECT * FROM products";
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
