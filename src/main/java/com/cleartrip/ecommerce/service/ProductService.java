package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.Product;
import com.cleartrip.ecommerce.model.Inventory;
import com.cleartrip.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // create product
    @Transactional
    public Product createProduct(Product product) {
        if (product.getInventory() != null) {
            product.getInventory().setProduct(product);
        } else {
            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(0);
            product.setInventory(inventory);
        }
        return productRepository.save(product);
    }

    // update product
    @Transactional
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
            .map(existingProduct -> {
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setDescription(updatedProduct.getDescription());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setCategory(updatedProduct.getCategory());
                
                // Update inventory if provided
                if (updatedProduct.getInventory() != null) {
                    existingProduct.getInventory().setQuantity(
                        updatedProduct.getInventory().getQuantity()
                    );
                }
                
                return productRepository.save(existingProduct);
            });
    }

    // delete product
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // get product by id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // search product by name
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // search product by category
    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }
} 