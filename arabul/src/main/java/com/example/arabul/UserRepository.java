package com.example.arabul;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean save(String name, String email, String phone, String password, String address, String profilePic) {

        String sql = "INSERT INTO users(name, email, phone, password, address, profile_picture) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(sql, name, email, phone, password, address, profilePic) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkIsEmailTaken(String email) {

        String sql = "SELECT COUNT(*) FROM users WHERE email=?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer check(String email, String password) {

        String sql = "SELECT id FROM users WHERE email=? AND password=?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, password);
            return count;
        } catch (Exception e) {
            return null;
        }
    }

}
