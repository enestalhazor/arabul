package com.example.arabul;

import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<?> GetProducts() {
        try {
            return ResponseEntity.ok(repository.products());
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error fetching products: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> GetProductById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(repository.productById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error fetching product: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteById(@PathVariable Integer id) {
        try {
            if (repository.deleteById(id)) {
                return ResponseEntity.ok("Product deleted");
            } else {
                return ResponseEntity.status(404).body("Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("DB error");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> GetProductByTerm(@RequestParam("term") String term) {
        try {
            return ResponseEntity.ok(repository.productByTerm(term));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error fetching product: " + e.getMessage());
        }
    }
}
