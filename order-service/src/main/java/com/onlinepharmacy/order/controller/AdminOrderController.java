package com.onlinepharmacy.order.controller;

import com.onlinepharmacy.order.dto.request.InternalPrescriptionUpdateDto;
import com.onlinepharmacy.order.dto.request.UpdateOrderStatusRequestDto;
import com.onlinepharmacy.order.dto.response.OrderDetailsResponseDto;
import com.onlinepharmacy.order.dto.response.SalesReportResponseDto;
import com.onlinepharmacy.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal REST Controller for administrative order operations.
 * author: Rohan kumar
 */
@RestController
@RequestMapping("/api/internal/admin")
public class AdminOrderController {

  private final OrderService orderService;

  public AdminOrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * Updates the status of a specific order.
   *
   * @param orderId the ID of the order to update
   * @param req the request containing the new status
   * @return the updated order details
   */
  @PutMapping("/orders/{id}/status")
  public OrderDetailsResponseDto adminUpdateOrderStatus(
      @PathVariable("id") Long orderId,
      @Valid @RequestBody UpdateOrderStatusRequestDto req
  ) {
    return orderService.adminUpdateStatus(orderId, req.getStatus());
  }

  /**
   * Generates and retrieves the sales report.
   *
   * @return the sales report response
   */
  @GetMapping("/reports/sales")
  public SalesReportResponseDto getSalesReport() {
    return orderService.getSalesReport();
  }

  /**
   * Internal endpoint to update the prescription status for an order.
   *
   * @param req the internal prescription update request
   */
  @PostMapping("/orders/prescription-status")
  public void updatePrescriptionStatus(@Valid @RequestBody InternalPrescriptionUpdateDto req) {
    orderService.handlePrescriptionUpdate(req.getOrderId(), req.getStatus());
  }

  /**
   * Retrieves the total count of orders placed today.
   *
   * @return the number of orders placed today
   */
  @GetMapping("/stats/today-orders")
  public long getTodayOrderCount() {
    return orderService.getTodayOrderCount();
  }
}
