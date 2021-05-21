package com.jc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
@RestController
public class ProductController {

    @GetMapping("/")
    public List<Product> getProducts() {
        return List.of(new Product());
    }

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable String productId) {
        return new Product();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity deleteProduct(@PathVariable String productId) {
        return ResponseEntity.ok().build();
    }

}
