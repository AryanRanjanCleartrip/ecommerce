package com.cleartrip.ecommerce.service;

import com.cleartrip.ecommerce.model.Inventory;
// import com.cleartrip.ecommerce.model.Product;
import com.cleartrip.ecommerce.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductService productService;

    public Optional<Inventory> addStock(Long productId, Integer quantity) {
        return productService.getProductById(productId)
                .map(product -> {
                    Optional<Inventory> existingInventory = inventoryRepository.findByProduct(product);
                    Inventory inventory;
                    if (existingInventory.isPresent()) {
                        inventory = existingInventory.get();
                        inventory.setQuantity(inventory.getQuantity() + quantity);
                    } else {
                        inventory = new Inventory();
                        inventory.setProduct(product);
                        inventory.setQuantity(quantity);
                    }
                    return inventoryRepository.save(inventory);
                });
    }

    public Optional<Inventory> updateStock(Long productId, Integer quantity) {
        return productService.getProductById(productId)
                .flatMap(product -> {
                    Optional<Inventory> existingInventory = inventoryRepository.findByProduct(product);
                    if (existingInventory.isPresent()) {
                        Inventory inventory = existingInventory.get();
                        inventory.setQuantity(quantity);
                        return Optional.of(inventoryRepository.save(inventory));
                    }
                    return Optional.empty();
                });
    }

    public boolean deleteStock(Long productId) {
        return productService.getProductById(productId)
                .map(product -> {
                    Optional<Inventory> inventory = inventoryRepository.findByProduct(product);
                    if (inventory.isPresent()) {
                        inventoryRepository.delete(inventory.get());
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryByProduct(Long productId) {
        return productService.getProductById(productId)
                .flatMap(inventoryRepository::findByProduct);
    }
}