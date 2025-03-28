package com.cleartrip.ecommerce.controller;

import com.cleartrip.ecommerce.model.*;
import com.cleartrip.ecommerce.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("testUser");

        testProduct = new Product();
        testProduct.setId(1L);

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUser(testUser);
        testCart.setItems(new ArrayList<>());
    }

    @Test
    void addToCart_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));
        when(cartService.addToCart(any(Long.class), any(Long.class), any(Integer.class))).thenReturn(Optional.of(testCart));

        mockMvc.perform(post("/api/cart/1/add/1")
                        .param("quantity", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCart_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(cartService.getCartByUser(1L)).thenReturn(Optional.of(testCart));

        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk());
    }
}