package com.onlinepharmacy.admin.controller;

import com.onlinepharmacy.admin.client.CatalogAdminClient;
import com.onlinepharmacy.admin.dto.response.SalesReportDto;
import com.onlinepharmacy.admin.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for consolidating administrative reports across microservices.
 */
@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

  private final AdminService adminService;

  public AdminReportController(AdminService adminService) {
    this.adminService = adminService;
  }

  /**
   * Aggregates and retrieves the global sales report.
   *
   * @return the consolidated sales report response
   */
  @GetMapping("/sales")
  public SalesReportDto salesReport() {
    return adminService.getSalesReport();
  }

  /**
   * Generates a report showing the volume of uploaded prescriptions.
   *
   * @return a map containing prescription volume statistics
   */
  @GetMapping("/prescriptions/volume")
  public Map<String, Long> getPrescriptionVolume() {
    return adminService.getPrescriptionVolume();
  }

  /**
   * Retrieves a list of expiring medicine batches for administrative review.
   *
   * @return a list of expiring batch details
   */
  @GetMapping("/batches/expiring")
  public List<CatalogAdminClient.BatchDto> getExpiringBatches() {
    return adminService.getExpiringBatches();
  }
}
