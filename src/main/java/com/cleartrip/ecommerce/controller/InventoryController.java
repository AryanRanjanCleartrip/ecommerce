package com.cleartrip.ecommerce.controller;

import com.cleartrip.ecommerce.model.Inventory;
import com.cleartrip.ecommerce.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    // add stock to the inventory
    @PostMapping("/{productId}")
    public ResponseEntity<?> addStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        return inventoryService.addStock(productId, quantity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // update stock in the inventory
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        return inventoryService.updateStock(productId, quantity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // delete stock from the inventory
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteStock(@PathVariable Long productId) {
        boolean deleted = inventoryService.deleteStock(productId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // get all inventory
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    // get inventory by product
    @GetMapping("/{productId}")
    public ResponseEntity<?> getInventoryByProduct(@PathVariable Long productId) {
        return inventoryService.getInventoryByProduct(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}