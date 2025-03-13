package com.cleartrip.ecommerce.repository;

import com.cleartrip.ecommerce.model.Order;
import com.cleartrip.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
} 