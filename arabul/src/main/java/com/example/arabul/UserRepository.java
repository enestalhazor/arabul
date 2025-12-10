package com.example.arabul;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public boolean save(String name, String email, String phone, String password, String address, String profilePic) {

        String sql = "INSERT INTO users(name, email, phone, password, address, profile_picture) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(sql, name, email, phone, password, address, profilePic) > 0;
        } catch (Exception e) {
            e.printStackTrace();
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

    public Map<String, Object> userById(Integer id) {

        String sql = "SELECT * FROM users WHERE id=?";
        try {
            return jdbcTemplate.queryForMap(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    public Integer getIdByEmail(String email) {

        String sql = "SELECT id FROM users WHERE email=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, email);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Integer editUserInfo(Integer id, String name, String email, String phone, String password, String address, String profilePic) {

        try {
            StringBuilder sql = new StringBuilder("UPDATE users SET ");
            List<Object> params = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                sql.append("name=?, ");
                params.add(name);
            }
            if (email != null && !email.isBlank()) {
                sql.append("email=?, ");
                params.add(email);
            }
            if (phone != null && !phone.isBlank()) {
                sql.append("phone=?, ");
                params.add(phone);
            }
            if (password != null && !password.isBlank()) {
                sql.append("password=?, ");
                params.add(password);
            }
            if (address != null && !address.isBlank()) {
                sql.append("address=?, ");
                params.add(address);
            }
            if (profilePic != null && !profilePic.isBlank()) {
                sql.append("profile_picture=?, ");
                params.add(profilePic);
            }
            if (params.isEmpty()) {
                return -1;
            }

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE id=?");
            params.add(id);

            return jdbcTemplate.update(sql.toString(), params.toArray()) > 0 ? 1 : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
