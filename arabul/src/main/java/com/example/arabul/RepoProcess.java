package com.example.arabul;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
