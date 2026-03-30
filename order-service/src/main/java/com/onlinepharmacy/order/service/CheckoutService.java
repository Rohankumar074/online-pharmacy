package com.onlinepharmacy.order.service;

import com.onlinepharmacy.order.amqp.OrderEventPublisher;
import com.onlinepharmacy.order.client.CatalogClient;
import com.onlinepharmacy.order.dto.AddressDto;
import com.onlinepharmacy.order.dto.response.OrderCheckoutResponseDto;
import com.onlinepharmacy.order.dto.response.OrderPaymentResponseDto;
import com.onlinepharmacy.order.exception.ConflictException;
import com.onlinepharmacy.order.exception.ForbiddenException;
import com.onlinepharmacy.order.exception.ResourceNotFoundException;
import com.onlinepharmacy.order.mapper.OrderMapper;
import com.onlinepharmacy.order.model.Order;
import com.onlinepharmacy.order.model.OrderStatus;
import com.onlinepharmacy.order.repo.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
  private final OrderRepository orderRepository;
  private final CatalogClient catalogClient;
  private final OrderEventPublisher orderEventPublisher;

  public CheckoutService(OrderRepository orderRepository, CatalogClient catalogClient, OrderEventPublisher orderEventPublisher) {
    this.orderRepository = orderRepository;
    this.catalogClient = catalogClient;
    this.orderEventPublisher = orderEventPublisher;
  }

  @Transactional
  public OrderCheckoutResponseDto checkoutStart(String customerId, AddressDto addressDto) {
    Order order = orderRepository.findTopByCustomerIdAndStatus(customerId, OrderStatus.DRAFT_CART)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

    order.setAddress(OrderMapper.toAddressEntity(addressDto));

    boolean requiresRx = order.getItems().stream()
        .anyMatch(i -> catalogClient.getMedicine(i.getMedicineId()).requiresPrescription());

    order.setStatus(requiresRx ? OrderStatus.PRESCRIPTION_PENDING : OrderStatus.PAYMENT_PENDING);
    order.recalculateTotal();
    orderRepository.save(order);

    try {
      orderEventPublisher.publishStatusChanged(order.getId(), order.getCustomerId(), order.getStatus().name());
    } catch (Exception e) {
      org.slf4j.LoggerFactory.getLogger(CheckoutService.class)
          .warn("Could not publish checkout event (RabbitMQ unavailable?): {}", e.getMessage());
    }

    return new OrderCheckoutResponseDto(order.getId(), order.getStatus());
  }

  @Transactional
  public OrderPaymentResponseDto initiatePayment(String customerId, Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if (!order.getCustomerId().equals(customerId)) {
      throw new ForbiddenException("Not allowed");
    }

    if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
      throw new ConflictException("Payment not available in current state: " + order.getStatus());
    }

    order.setStatus(OrderStatus.PAID);
    orderRepository.save(order);
    try {
      orderEventPublisher.publishStatusChanged(order.getId(), order.getCustomerId(), order.getStatus().name());
    } catch (Exception e) {
      org.slf4j.LoggerFactory.getLogger(CheckoutService.class)
          .warn("Could not publish payment event (RabbitMQ unavailable?): {}", e.getMessage());
    }

    return new OrderPaymentResponseDto(order.getId(), order.getStatus(), "PAYMENT_SUCCESS_STUB");
  }
}
