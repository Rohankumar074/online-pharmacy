package com.onlinepharmacy.order.service;

import com.onlinepharmacy.order.client.CatalogClient;
import com.onlinepharmacy.order.dto.response.OrderCartResponseDto;
import com.onlinepharmacy.order.exception.ConflictException;
import com.onlinepharmacy.order.exception.ResourceNotFoundException;
import com.onlinepharmacy.order.mapper.OrderMapper;
import com.onlinepharmacy.order.model.Order;
import com.onlinepharmacy.order.model.OrderItem;
import com.onlinepharmacy.order.model.OrderStatus;
import com.onlinepharmacy.order.repo.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CartService {
  private final OrderRepository orderRepository;
  private final CatalogClient catalogClient;

  public CartService(OrderRepository orderRepository, CatalogClient catalogClient) {
    this.orderRepository = orderRepository;
    this.catalogClient = catalogClient;
  }

  @Transactional
  public OrderCartResponseDto addToCart(String customerId, Long medicineId, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }

    Order order = orderRepository.findTopByCustomerIdAndStatus(customerId, OrderStatus.DRAFT_CART)
        .orElseGet(() -> createDraftOrder(customerId));

    CatalogClient.CatalogMedicineDto med = catalogClient.getMedicine(medicineId);
    if (med.stock() < quantity) {
      throw new ConflictException("Not enough stock");
    }

    OrderItem item = order.getItems().stream()
        .filter(i -> i.getMedicineId().equals(medicineId))
        .findFirst()
        .orElse(null);

    if (item == null) {
      item = new OrderItem();
      item.setOrder(order);
      item.setMedicineId(med.id());
      item.setMedicineName(med.name());
      item.setQuantity(quantity);
      item.setUnitPrice(med.price());
      order.getItems().add(item);
    } else {
      int newQty = item.getQuantity() + quantity;
      if (med.stock() < newQty) {
        throw new ConflictException("Not enough stock for updated quantity");
      }
      item.setQuantity(newQty);
      item.setUnitPrice(med.price());
    }

    order.recalculateTotal();
    orderRepository.save(order);

    return OrderMapper.toCartResponseDto(order);
  }

  @Transactional
  public OrderCartResponseDto updateQuantity(String customerId, Long medicineId, int quantity) {
    Order order = orderRepository.findTopByCustomerIdAndStatus(customerId, OrderStatus.DRAFT_CART)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

    OrderItem item = order.getItems().stream()
        .filter(i -> i.getMedicineId().equals(medicineId))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Item not in cart"));

    if (quantity <= 0) {
      order.getItems().remove(item);
    } else {
      CatalogClient.CatalogMedicineDto med = catalogClient.getMedicine(medicineId);
      if (med.stock() < quantity) {
        throw new ConflictException("Not enough stock");
      }
      item.setQuantity(quantity);
    }

    order.recalculateTotal();
    orderRepository.save(order);
    return OrderMapper.toCartResponseDto(order);
  }

  @Transactional
  public OrderCartResponseDto toggleSubstitution(String customerId, boolean enabled) {
    Order order = orderRepository.findTopByCustomerIdAndStatus(customerId, OrderStatus.DRAFT_CART)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

    order.setSubstitutionEnabled(enabled);
    orderRepository.save(order);
    return OrderMapper.toCartResponseDto(order);
  }

  @Transactional
  public OrderCartResponseDto getCart(String customerId) {
    Order order = orderRepository.findTopByCustomerIdAndStatus(customerId, OrderStatus.DRAFT_CART)
        .orElseThrow(() -> new ResourceNotFoundException("Cart is Empty"));
    return OrderMapper.toCartResponseDto(order);
  }

  private Order createDraftOrder(String customerId) {
    Order order = new Order();
    order.setCustomerId(customerId);
    order.setStatus(OrderStatus.DRAFT_CART);
    order.recalculateTotal();
    return orderRepository.save(order);
  }
}
