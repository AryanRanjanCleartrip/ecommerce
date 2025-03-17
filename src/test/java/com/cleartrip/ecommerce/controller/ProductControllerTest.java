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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Arrays;
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
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/products")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getAllProducts_Success() throws Exception {
        Page<Product> page = new PageImpl<>(Arrays.asList(testProduct));
        when(productService.getAllProducts(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Product"));
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
        Page<Product> page = new PageImpl<>(Arrays.asList(testProduct));
        when(productService.filterProducts("Electronics", 50.0, 100.0, 0, 10))
                .thenReturn(page);

        mockMvc.perform(get("/api/products/filter")
                .param("category", "Electronics")
                .param("minPrice", "50.0")
                .param("maxPrice", "100.0")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Product"));
    }
}
