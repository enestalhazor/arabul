package com.example.arabul;

import com.auth0.jwt.JWT;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                    return ResponseEntity.status(400).body(Map.of("info", "This is not JPEG file"));
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
                return ResponseEntity.status(400).body(Map.of("info", "Missing required fields"));
            }

            if (repository.checkIsEmailTaken(email)) {
                return ResponseEntity.status(422).body(Map.of("info", "Email taken"));
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return ResponseEntity.status(400).body(Map.of("info", "Invalid email format"));
            }

            if (password.length() < 4) {
                return ResponseEntity.status(400).body(Map.of("info", "Password length should be more than 4 character"));
            }

            if (phone != null || !phone.isBlank()) {
                if (phone.matches("^\\\\d{3}-\\\\d{3}-\\\\d{4}$")) {
                    return ResponseEntity.status(400).body(Map.of("info", "Invalid phone format"));
                }
            }

            String hashedPassword = HashService.hashPassword(password);

            if (repository.save(name, email, phone, hashedPassword, address, fileName)) {
                return ResponseEntity.ok().body(Map.of("info", "User ınserted"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("info", "Bad Request"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("info", "Bad request " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest request) throws NoSuchAlgorithmException {

        if (request.getPassword() == null || request.getPassword().isBlank() ||
                request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.status(400).body(Map.of("info", "Missing required fields"));
        }
        try {
            String hashedPassword = HashService.hashPassword(request.getPassword());
            Integer id = repository.check(request.getEmail(), hashedPassword);
            if (id != null || id > 0) {

                String email = request.getEmail();
                String token = JWTService.create(email, id);

                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("token", token);

                return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(responseBody);
            }

            return ResponseEntity.status(404).body("User not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> GetUserInfo(@RequestHeader(value = "Authorization", required = false) String authHeader, @PathVariable Integer id) throws NoSuchAlgorithmException {

        if (!JWTService.checkToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("info", "Missing or invalid Authorization header"));
        }

        try {
            var products = repository.userById(id);

            if (products == null || products.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("info", "No user found for id: " + id));
            }

            Integer userid = RequestContext.getUserId();

            if (Objects.equals(userid, id)) {
                return ResponseEntity.ok(products);
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> EditUserInfo(@PathVariable Integer id,
                                          @RequestHeader(value = "Authorization", required = false) String authHeader,
                                          @RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "email", required = false) String email,
                                          @RequestParam(value = "phone", required = false) String phone,
                                          @RequestParam(value = "password", required = false) String password,
                                          @RequestParam(value = "address", required = false) String address,
                                          @RequestParam(value = "profile_picture", required = false) MultipartFile profilePic) throws NoSuchAlgorithmException, IOException {

        if (!JWTService.checkToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("info", "Missing or invalid Authorization header"));
        }

        String fileName = "";
        String hashedPassword = "";
        if (profilePic != null && !profilePic.isEmpty()) {

            String contentType = profilePic.getContentType();
            if (!"image/jpeg".equalsIgnoreCase(contentType)) {
                return ResponseEntity.status(400).body(Map.of("info", "This is not JPEG file"));
            }

            Path uploadDir = Paths.get(System.getProperty("user.dir"), "userphotos");
            Files.createDirectories(uploadDir);

            fileName = Paths.get(profilePic.getOriginalFilename()).getFileName().toString();
            Path uploadPath = uploadDir.resolve(fileName);
            profilePic.transferTo(uploadPath.toFile());
        }

        try {

            if (repository.checkIsEmailTaken(email)) {
                return ResponseEntity.status(422).body(Map.of("info", "Email taken"));
            }

            if (!email.isBlank() && email != null) {
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    return ResponseEntity.status(400).body(Map.of("info", "Invalid email format"));
                }
            }

            if (!password.isBlank() && password != null) {
                if (password.length() < 4) {
                    return ResponseEntity.status(400).body(Map.of("info", "Password length should be more than 4 character"));
                }
            }

            if (!phone.isBlank() && phone != null) {
                if (phone != null || !phone.isBlank()) {
                    if (phone.matches("^\\\\d{3}-\\\\d{3}-\\\\d{4}$")) {
                        return ResponseEntity.status(400).body(Map.of("info", "Invalid phone format"));
                    }
                }
            }

            if (!password.isBlank() && password != null) {
                hashedPassword = HashService.hashPassword(password);
            }

            Integer result = repository.editUserInfo(id, name, email, phone, hashedPassword, address, fileName);

            if (result == -1 || result == null) {
                return ResponseEntity.status(404).body(Map.of("info", "No user found for id: " + result));
            }

            Integer userId = RequestContext.getUserId();

            if (Objects.equals(userId, result)) {

                return ResponseEntity.ok(Map.of("info", "User infos edited id: " + result));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

}
