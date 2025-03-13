package com.cleartrip.ecommerce.repository;

import com.cleartrip.ecommerce.model.Cart;
import com.cleartrip.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
} 