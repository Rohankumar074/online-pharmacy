package com.onlinepharmacy.order.service;

import com.onlinepharmacy.order.amqp.OrderEventPublisher;
import com.onlinepharmacy.order.dto.response.OrderDetailsResponseDto;
import com.onlinepharmacy.order.dto.response.SalesReportResponseDto;
import com.onlinepharmacy.order.exception.ForbiddenException;
import com.onlinepharmacy.order.exception.ResourceNotFoundException;
import com.onlinepharmacy.order.mapper.OrderMapper;
import com.onlinepharmacy.order.model.Order;
import com.onlinepharmacy.order.model.OrderStatus;
import com.onlinepharmacy.order.repo.OrderRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderEventPublisher orderEventPublisher;

  public OrderService(
      OrderRepository orderRepository,
      OrderEventPublisher orderEventPublisher
  ) {
    this.orderRepository = orderRepository;
    this.orderEventPublisher = orderEventPublisher;
  }

  @Transactional
  public OrderDetailsResponseDto getOrder(String customerId, Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    if (!order.getCustomerId().equals(customerId)) {
      throw new ForbiddenException("Not allowed");
    }
    return OrderMapper.toDetailsResponseDto(order);
  }

  @Transactional
  public List<OrderDetailsResponseDto> listOrders(String customerId) {
    return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
        .map(OrderMapper::toDetailsResponseDto)
        .toList();
  }

  @Transactional
  public OrderDetailsResponseDto adminUpdateStatus(Long orderId, OrderStatus newStatus) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    order.setStatus(newStatus);
    order.recalculateTotal();
    orderRepository.save(order);

    try {
      orderEventPublisher.publishStatusChanged(order.getId(), order.getCustomerId(), newStatus.name());
    } catch (Exception e) {
      org.slf4j.LoggerFactory.getLogger(OrderService.class)
          .warn("Could not publish status-change event (RabbitMQ unavailable?): {}", e.getMessage());
    }

    return OrderMapper.toDetailsResponseDto(order);
  }

  /**
   * Listener hook: when a prescription is approved, move the latest pending order
   * from PRESCRIPTION_PENDING -> PAYMENT_PENDING.
   */
  @Transactional
  public void handlePrescriptionUpdate(Long orderId, OrderStatus status) {
    org.slf4j.LoggerFactory.getLogger(OrderService.class)
        .info("Received prescription update for orderId: {}, new status: {}", orderId, status);

    Order order = orderRepository.findById(orderId)
        .orElse(null);
    if (order == null) {
      org.slf4j.LoggerFactory.getLogger(OrderService.class)
          .error("Order NOT FOUND for orderId: {}. Cannot update status.", orderId);
      return;
    }
    
    OrderStatus oldStatus = order.getStatus();
    if (status == OrderStatus.PRESCRIPTION_APPROVED) {
      order.setStatus(OrderStatus.PAYMENT_PENDING);
    } else {
      order.setStatus(status);
    }
    orderRepository.save(order);
    
    org.slf4j.LoggerFactory.getLogger(OrderService.class)
        .info("Updated orderId: {} status from {} to {}", orderId, oldStatus, order.getStatus());

    // Notify via event (keeping for potential other consumers like notification-service)
    try {
      orderEventPublisher.publishStatusChanged(order.getId(), order.getCustomerId(), order.getStatus().name());
    } catch (Exception e) {
       org.slf4j.LoggerFactory.getLogger(OrderService.class)
          .error("Failed to publish status change event for orderId: {}", orderId, e);
    }
  }

  /**
   * Listener hook: when a prescription is approved.
   */
  @Transactional
  public void onPrescriptionApproved(Long orderId) {
    handlePrescriptionUpdate(orderId, OrderStatus.PRESCRIPTION_APPROVED);
  }

  /**
   * Listener hook: when a prescription is rejected.
   */
  @Transactional
  public void onPrescriptionRejected(Long orderId) {
    handlePrescriptionUpdate(orderId, OrderStatus.PRESCRIPTION_REJECTED);
  }

  /**
   * Sales report: count of paid/delivered orders and total revenue.
   */
  public SalesReportResponseDto getSalesReport() {
    List<Order> paidOrders = orderRepository.findByStatus(OrderStatus.PAID);
    List<Order> deliveredOrders = orderRepository.findByStatus(OrderStatus.DELIVERED);

    long totalOrders = paidOrders.size() + deliveredOrders.size();
    java.math.BigDecimal totalRevenue = java.util.stream.Stream.concat(paidOrders.stream(), deliveredOrders.stream())
        .map(Order::getTotalAmount)
        .filter(java.util.Objects::nonNull)
        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

    return new SalesReportResponseDto(totalOrders, totalRevenue);
  }

  public long getTodayOrderCount() {
    Instant startOfDay = java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
    return orderRepository.findAll().stream()
        .filter(o -> o.getCreatedAt().isAfter(startOfDay))
        .count();
  }
}
