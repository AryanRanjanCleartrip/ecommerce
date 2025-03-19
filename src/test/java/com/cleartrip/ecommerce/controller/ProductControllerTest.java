package com.cleartrip.ecommerce.controller;

import com.cleartrip.ecommerce.model.Product;
import com.cleartrip.ecommerce.model.User;
import com.cleartrip.ecommerce.model.UserRole;
import com.cleartrip.ecommerce.service.ProductService;
import com.cleartrip.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Product testProduct;
    private User adminUser;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setCategory("Electronics");

        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(UserRole.ADMIN);
    }

    @Test
    void createProduct_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(adminUser));
        when(productService.createProduct(any(Product.class), any(Long.class))).thenReturn(Optional.of(testProduct));

        mockMvc.perform(post("/api/products")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getAllProducts_Success() throws Exception {
        // Add logic to test getting all products
    }

    @Test
    void getProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void filterProducts_Success() throws Exception {
        // Add logic to test filtering products
    }
}