package com.onlinepharmacy.admin.controller;

import com.onlinepharmacy.admin.client.OrderAdminClient;
import com.onlinepharmacy.admin.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for handling administrative order operations.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

  private final AdminService adminService;

  public AdminOrderController(AdminService adminService) {
    this.adminService = adminService;
  }

  /**
   * Updates the status of an order from the administrative panel.
   *
   * @param id the ID of the order to update
   * @param req the status update request
   * @return the updated order details
   */
  @PutMapping("/orders/{id}/status")
  public OrderAdminClient.OrderDetailsDto updateOrderStatus(
      @PathVariable("id") Long id,
      @Valid @RequestBody OrderAdminClient.UpdateOrderStatusRequest req
  ) {
    return adminService.updateOrderStatus(id, req);
  }
}
