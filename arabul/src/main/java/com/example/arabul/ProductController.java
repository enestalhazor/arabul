package com.example.arabul;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final RepoProcess repository;

    public ProductController(RepoProcess repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> CreateProduct(@RequestParam String name, @RequestParam String description, @RequestParam(value = "photo", required = false) MultipartFile photo, @RequestParam Float price, @RequestParam String category) throws IOException {
        String fileName = "";

        try {

            if (photo != null && !photo.isEmpty()) {
                Path uploadDir = Paths.get(System.getProperty("user.dir"), "productphotos");
                Files.createDirectories(uploadDir);

                fileName = Paths.get(photo.getOriginalFilename()).getFileName().toString();
                Path uploadPath = uploadDir.resolve(fileName);
                photo.transferTo(uploadPath.toFile());
            }

            if (repository.save(name, description, fileName, price, category)) {
                return ResponseEntity.ok("Product inserted.");
            } else {
                return ResponseEntity.status(500).body("DB Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Bad request " + e.getMessage());
        }
    }
}
