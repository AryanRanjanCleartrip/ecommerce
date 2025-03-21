package com.cleartrip.ecommerce.controller;

import com.cleartrip.ecommerce.model.Order;
import com.cleartrip.ecommerce.model.OrderStatus;
import com.cleartrip.ecommerce.model.User;
import com.cleartrip.ecommerce.service.OrderService;
import com.cleartrip.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.cleartrip.ecommerce.model.UserRole.CUSTOMER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    private Order testOrder;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setRole(CUSTOMER);

        testOrder = new Order();
        testOrder.setId(10L);
        testOrder.setUser(user);
        testOrder.setOrderDate(LocalDateTime.of(2025, 3, 17, 10, 0));
        testOrder.setTotalAmount(100.0);
        testOrder.setStatus(OrderStatus.PENDING);
    }

    @Test
    void placeOrder_success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(orderService.placeOrder(1L)).thenReturn(Optional.of(testOrder));

        mockMvc.perform(post("/api/orders/1/place")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void getOrderById_success() throws Exception {
        when(orderService.getOrderById(10L)).thenReturn(Optional.of(testOrder));

        mockMvc.perform(get("/api/orders/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void getOrdersByUser_success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(orderService.getOrdersByUser(1L)).thenReturn(Collections.singletonList(testOrder));

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));
    }
}