package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.*;
import com.cleartrip.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public Optional<Order> placeOrder(User user) {
        Optional<Cart> cartOptional = cartService.getCartByUser(user);
        if (cartOptional.isPresent() && !cartOptional.get().getItems().isEmpty()) {
            Cart cart = cartOptional.get();
            
            // Check inventory and calculate total
            double totalAmount = 0;
            for (CartItem cartItem : cart.getItems()) {
                Optional<Inventory> inventoryOptional = inventoryService.getInventoryByProduct(cartItem.getProduct());
                if (inventoryOptional.isEmpty() || inventoryOptional.get().getQuantity() < cartItem.getQuantity()) {
                    return Optional.empty();
                }
                totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            }

            // Create order
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.PENDING);
            order.setItems(new ArrayList<>());

            // Create order items and update inventory
            for (CartItem cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProduct().getPrice());
                order.getItems().add(orderItem);

                // Update inventory
                Inventory inventory = inventoryService.getInventoryByProduct(cartItem.getProduct()).get();
                inventory.setQuantity(inventory.getQuantity() - cartItem.getQuantity());
                inventoryService.updateStock(cartItem.getProduct(), inventory.getQuantity());
            }

            // Save order and clear cart
            Order savedOrder = orderRepository.save(order);
            cartService.clearCart(cart);
            return Optional.of(savedOrder);
        }
        return Optional.empty();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
} 