package com.cleartrip.ecommerce.controller;

// import com.cleartrip.ecommerce.model.Cart;
import com.cleartrip.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Object> addToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return cartService.addToCart(userId, productId, quantity)
                .map(cart -> ResponseEntity.ok().body((Object) cart))
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "Failed to add item to cart")));
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        return cartService.removeFromCart(userId, productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}