package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.Product;
import com.cleartrip.ecommerce.model.Inventory;
import com.cleartrip.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setCategory("Electronics");
        
        Inventory inventory = new Inventory();
        inventory.setQuantity(10);
        testProduct.setInventory(inventory);
    }

    @Test
    void createProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product created = productService.createProduct(testProduct);

        assertNotNull(created);
        assertEquals("Test Product", created.getName());
        assertEquals(99.99, created.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> found = productService.getProductById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test Product", found.get().getName());
    }

    @Test
    void getAllProducts_Success() {
        Page<Product> page = new PageImpl<>(Arrays.asList(testProduct));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Product> result = productService.getAllProducts(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void filterProducts_Success() {
        Page<Product> page = new PageImpl<>(Arrays.asList(testProduct));
        when(productRepository.findByFilters(anyString(), anyDouble(), anyDouble(), any(PageRequest.class)))
            .thenReturn(page);

        Page<Product> result = productService.filterProducts("Electronics", 50.0, 100.0, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}
