package com.example.arabul;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
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

            if (repository.save(name, description, fileName, price.toString(), category)) {
                return ResponseEntity.ok(Map.of("info", "Product inserted."));
            } else {
                return ResponseEntity.status(500).body(Map.of("info", "DB Error"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("info", "Bad request " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> GetProducts() {
        try {
            List<Map<String, Object>> products = repository.products();
            if (products == null) {
                return ResponseEntity.status(404).body(Map.of("info", "No products found"));
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("info", "Error fetching products: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> GetProductById(@PathVariable Integer id) {
        try {
            var products = repository.productById(id);
            if (products == null || products.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("info", "No products found for id: " + id));
            }
            return ResponseEntity.ok(repository.productById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("info", "Error fetching product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteById(@PathVariable Integer id) {
        try {
            if (repository.deleteById(id)) {
                return ResponseEntity.ok("Product deleted");
            } else {
                return ResponseEntity.status(404).body(Map.of("info","Not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(Map.of("info", "DB error"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> GetProductByTerm(@RequestParam("term") String term) {
        try {
            var products = repository.productByTerm(term);
            if (products == null || products.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("info", "No products found for term: " + term));
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("info", "Error fetching product: " + e.getMessage()));
        }
    }
}
