package com.onlinepharmacy.admin.controller;

import com.onlinepharmacy.admin.client.CatalogAdminClient.AdminDashboard;
import com.onlinepharmacy.admin.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for retrieving consolidated administrative dashboard data.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

  private final AdminService adminService;

  public AdminDashboardController(AdminService adminService) {
    this.adminService = adminService;
  }

  /**
   * Retrieves dashboard statistics across all microservices.
   *
   * @return the dashboard data model
   */
  @GetMapping("/dashboard")
  public AdminDashboard dashboard() {
    return adminService.getDashboardData();
  }
}
