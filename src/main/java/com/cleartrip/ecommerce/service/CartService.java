package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.*;
import com.cleartrip.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public Optional<Cart> getOrCreateCart(Long userId) {
        return userService.getUserById(userId)
                .flatMap(user -> {
                    Optional<Cart> existingCart = cartRepository.findByUser(user);
                    if (existingCart.isPresent()) {
                        return existingCart;
                    } else {
                        Cart newCart = new Cart();
                        newCart.setUser(user);
                        newCart.setItems(new ArrayList<>());
                        return Optional.of(cartRepository.save(newCart));
                    }
                });
    }

    public Optional<Cart> addToCart(Long userId, Long productId, Integer quantity) {
        return getOrCreateCart(userId)
                .flatMap(cart -> productService.getProductById(productId)
                        .flatMap(product -> {
                            Optional<Inventory> inventoryOptional = inventoryService.getInventoryByProduct(productId);
                            if (inventoryOptional.isEmpty() || inventoryOptional.get().getQuantity() < quantity) {
                                return Optional.empty();
                            }

                            Optional<CartItem> existingItem = cart.getItems().stream()
                                    .filter(item -> item.getProduct().getId().equals(productId))
                                    .findFirst();

                            if (existingItem.isPresent()) {
                                existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
                            } else {
                                CartItem newItem = new CartItem();
                                newItem.setCart(cart);
                                newItem.setProduct(product);
                                newItem.setQuantity(quantity);
                                cart.getItems().add(newItem);
                            }

                            return Optional.of(cartRepository.save(cart));
                        }));
    }

    public Optional<Cart> removeFromCart(Long userId, Long productId) {
        return userService.getUserById(userId)
                .flatMap(user -> cartRepository.findByUser(user)
                        .flatMap(cart -> productService.getProductById(productId)
                                .map(product -> {
                                    cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
                                    return cartRepository.save(cart);
                                })));
    }

    public Optional<Cart> getCartByUser(Long userId) {
        return userService.getUserById(userId).flatMap(cartRepository::findByUser);
    }

    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}