package com.example.arabul;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }


    @PostMapping
    public ResponseEntity<?> Register(@RequestParam String name, @RequestParam String email, @RequestParam(value = "phone", required = false) String phone, @RequestParam String password, @RequestParam String address, @RequestParam(value = "profile_picture", required = false) MultipartFile profilePic) throws IOException {
        String fileName = "";

        try {

            if (profilePic != null && !profilePic.isEmpty()) {

                String contentType = profilePic.getContentType();
                if (!"image/jpeg".equalsIgnoreCase(contentType)) {
                    return ResponseEntity.status(400).body("This is not JPEG file");
                }

                Path uploadDir = Paths.get(System.getProperty("user.dir"), "userphotos");
                Files.createDirectories(uploadDir);

                fileName = Paths.get(profilePic.getOriginalFilename()).getFileName().toString();
                Path uploadPath = uploadDir.resolve(fileName);
                profilePic.transferTo(uploadPath.toFile());
            }

            if (name == null || name.isBlank() ||
                    email == null || email.isBlank() ||
                    password == null || password.isBlank() ||
                    address == null || address.isBlank()) {
                return ResponseEntity.status(400).body("Missing required fields");
            }

            if (repository.checkIsEmailTaken(email)) {
                return ResponseEntity.status(422).body("Email taken");
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return ResponseEntity.status(400).body("Invalid email format");
            }

            if (password.length() < 4) {
                return ResponseEntity.status(400).body("Password length should be more than 4 character");
            }

            if (phone != null || !phone.isBlank()) {
                if (phone.matches("^\\\\d{3}-\\\\d{3}-\\\\d{4}$")) {
                    return ResponseEntity.status(400).body("Invalid phone format");
                }
            }

            String hashedPassword = HashProcess.hashPassword(password);

            if (repository.save(name, email, phone, hashedPassword, address, fileName)) {
                return ResponseEntity.ok().body("User ınserted");
            } else {
                return ResponseEntity.badRequest().body("Bad Request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Bad request " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest request) throws NoSuchAlgorithmException {
        try {

            if (request.getPassword() == null || request.getPassword().isBlank() ||
                    request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.status(400).body("Missing required fields");
            }

            String hashedPassword = HashProcess.hashPassword(request.getPassword());
            Integer id = repository.check(request.getEmail(), hashedPassword);
            if (id != null || id > 0) {

                String email = request.getEmail();
                String token = JWTProcess.jwtCreate(email, id);

                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("token", token);

                return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("User not found");
        }
        return null;
    }

}
