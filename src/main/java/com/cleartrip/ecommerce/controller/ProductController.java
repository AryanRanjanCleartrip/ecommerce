package com.cleartrip.ecommerce.controller;

import com.cleartrip.ecommerce.model.Product;
import com.cleartrip.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.cleartrip.ecommerce.model.User;
import com.cleartrip.ecommerce.model.UserRole;
import com.cleartrip.ecommerce.service.UserService;
    
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // create product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestParam Long userId) {
        return userService.getUserById(userId)
            .filter(user -> user.getRole() == UserRole.ADMIN)
            .map(user -> ResponseEntity.ok(productService.createProduct(product)))
            .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    // update product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product, @RequestParam Long userId) {
        return userService.getUserById(userId)
            .filter(user -> user.getRole() == UserRole.ADMIN)
            .map(user -> productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()))
            .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    // delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestParam Long userId) {
        return userService.getUserById(userId)
            .filter(user -> user.getRole() == UserRole.ADMIN)
            .map(user -> productService.deleteProduct(id) 
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build())
            .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    // get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam Long userId) {
        return userService.getUserById(userId)
            .filter(user -> user.getRole() == UserRole.ADMIN)
            .map(user -> ResponseEntity.ok(productService.getAllProducts()))
            .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    // get product by id    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // search product by category
    @GetMapping("/search/category")
    public ResponseEntity<List<Product>> searchByCategory(@RequestParam String category) {
        return ResponseEntity.ok(productService.searchByCategory(category));
    }

    // filter products
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(productService.filterProducts(category, minPrice, maxPrice));
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Product>>sortProducts(@RequestParam(required = true) String order){
        return ResponseEntity.ok(productService.sortProducts(order));
    }
} 