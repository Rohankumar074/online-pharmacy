package com.onlinepharmacy.order.repo;

import com.onlinepharmacy.order.model.Order;
import com.onlinepharmacy.order.model.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findTopByCustomerIdAndStatus(String customerId, OrderStatus status);
  List<Order> findByCustomerIdOrderByCreatedAtDesc(String customerId);
  List<Order> findByStatus(OrderStatus status);
  long countByStatus(OrderStatus status);
}

