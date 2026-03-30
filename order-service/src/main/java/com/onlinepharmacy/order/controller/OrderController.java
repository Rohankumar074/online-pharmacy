package com.onlinepharmacy.order.controller;

import com.onlinepharmacy.order.dto.response.OrderDetailsResponseDto;
import com.onlinepharmacy.order.service.OrderService;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for retrieving customer order history.
 */
@RestController
@RequestMapping("/api")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }



  // ---------------------------
  // Customer: Orders
  // ---------------------------

  /**
   * Retrieves details of a specific order for the authenticated customer.
   *
   * @param authentication the current user's authentication
   * @param orderId the ID of the order
   * @return the order details response
   */
  @GetMapping("/orders/{id}")
  public OrderDetailsResponseDto getOrder(Authentication authentication, @PathVariable("id") Long orderId) {
    String customerId = authentication.getName();
    return orderService.getOrder(customerId, orderId);
  }

  /**
   * Lists all orders placed by the authenticated customer.
   *
   * @param authentication the current user's authentication
   * @return a list of order details responses
   */
  @GetMapping("/orders")
  public List<OrderDetailsResponseDto> listOrders(Authentication authentication) {
    String customerId = authentication.getName();
    return orderService.listOrders(customerId);
  }
}
