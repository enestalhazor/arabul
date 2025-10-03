package com.example.arabul;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> CreateProduct(@RequestParam String name, @RequestParam String email, @RequestParam(value = "phone", required = false) String phone, @RequestParam String password, @RequestParam String address, @RequestParam(value = "profile_picture", required = false) MultipartFile profilePic) throws IOException {
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

            if (repository.save(name, email, phone, password, address, fileName)) {
                return ResponseEntity.ok().body("User ınserted");
            } else {
                return ResponseEntity.badRequest().body("Bad Request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Bad request " + e.getMessage());
        }
    }

}
