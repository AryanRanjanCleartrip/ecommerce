package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.*;
import com.cleartrip.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

// import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("testuser");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setPrice(99.99);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(testProduct);
        cartItem.setQuantity(1);

        testCart = new Cart();
        testCart.setUser(testUser);
        testCart.setItems(Arrays.asList(cartItem));

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setStatus(OrderStatus.PENDING);
    }

    @Test
    void placeOrder_Success() {
        Inventory inventory = new Inventory();
        inventory.setQuantity(10);

        // when(cartService.getCartByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartService.getCartByUser(1L)).thenReturn(Optional.of(testCart));
        when(inventoryService.getInventoryByProduct(any())).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Optional<Order> result = orderService.placeOrder(testUser);
        Optional<Order> result = orderService.placeOrder(1L);

        assertTrue(result.isPresent());
        assertEquals(OrderStatus.PENDING, result.get().getStatus());
        verify(cartService).clearCart(any(Cart.class));
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Optional<Order> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getUser().getId());
    }

    @Test
    void getOrdersByUser_Success() {
        when(orderRepository.findByUserOrderByOrderDateDesc(testUser))
                .thenReturn(Arrays.asList(testOrder));

        // List<Order> results = orderService.getOrdersByUser(testUser);
        List<Order> results = orderService.getOrdersByUser(1L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
}