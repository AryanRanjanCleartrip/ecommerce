package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.*;
import com.cleartrip.ecommerce.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private InventoryService inventoryService;

    @Autowired
    private CartService cartService;


    private User testUser;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("testuser");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUser(testUser);
        testCart.setItems(new ArrayList<>());
    }

    @Test
    void getOrCreateCart_ExistingCart() {
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        Optional<Cart> result = cartService.getOrCreateCart(testUser);

        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getUser().getId());
    }

    @Test
    void addToCart_Success() {
        Inventory inventory = new Inventory();
        inventory.setQuantity(10);
        testProduct.setInventory(inventory);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(inventoryService.getInventoryByProduct(testProduct)).thenReturn(Optional.of(inventory));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Optional<Cart> result = cartService.addToCart(testUser, testProduct, 1);

        assertTrue(result.isPresent());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void removeFromCart_Success() {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(testProduct);
        testCart.getItems().add(cartItem);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Optional<Cart> result = cartService.removeFromCart(testUser, testProduct);

        assertTrue(result.isPresent());
        assertTrue(result.get().getItems().isEmpty());
    }
}