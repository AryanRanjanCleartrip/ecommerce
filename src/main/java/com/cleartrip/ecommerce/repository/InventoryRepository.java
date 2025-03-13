package com.cleartrip.ecommerce.repository;

import com.cleartrip.ecommerce.model.Inventory;
import com.cleartrip.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
} 