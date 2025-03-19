package com.cleartrip.ecommerce.service;
//add exceptions
import com.cleartrip.ecommerce.model.Product;
import com.cleartrip.ecommerce.model.Inventory;
import com.cleartrip.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Optional<Product> createProduct(Product product, Long userId) {
        // Check if user is authorized to create a product
        if (userService.getUserById(userId).isPresent()) {
            if (product.getInventory() != null) {
                product.getInventory().setProduct(product);
            } else {
                Inventory inventory = new Inventory();
                inventory.setProduct(product);
                inventory.setQuantity(0);
                product.setInventory(inventory);
            }
            return Optional.of(productRepository.save(product));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Product> updateProduct(Long id, Product updatedProduct, Long userId) {
        // Check if user is authorized to update a product
        if (userService.getUserById(userId).isPresent()) {
            return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setCategory(updatedProduct.getCategory());
                    
                    // Updating the inventory if provided
                    if (updatedProduct.getInventory() != null) {
                        existingProduct.getInventory().setQuantity(
                            updatedProduct.getInventory().getQuantity()
                        );
                    }
                    
                    return productRepository.save(existingProduct);
                });
        }
        return Optional.empty();
    }

    public boolean deleteProduct(Long id, Long userId) {
        // Check if user is authorized to delete a product
        if (userService.getUserById(userId).isPresent() && productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    public Page<Product> filterProducts(
            String category, 
            Double minPrice, 
            Double maxPrice, 
            int page, 
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByFilters(category, minPrice, maxPrice, pageable);
    }

    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public List<Product> sortProducts(String order){
        if(order.equals("asc")){
            return productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        }else{
            return productRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        }
    }
} 