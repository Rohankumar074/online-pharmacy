package com.onlinepharmacy.admin.controller;

import com.onlinepharmacy.admin.client.CatalogAdminClient;
import com.onlinepharmacy.admin.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for centralizing administrative medicine and prescription operations.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminMedicineController {

  private final AdminService adminService;

  public AdminMedicineController(AdminService adminService) {
    this.adminService = adminService;
  }

  /**
   * Creates a new medicine in the global catalog.
   *
   * @param req the admin medicine creation request
   * @return the created medicine details
   */
  @PostMapping("/medicines")
  public CatalogAdminClient.MedicineDto createMedicine(@Valid @RequestBody CatalogAdminClient.AdminMedicineRequestDto req) {
    return adminService.createMedicine(req);
  }

  /**
   * Approves a specific user prescription upload.
   *
   * @param id the ID of the prescription to approve
   */
  @PutMapping("/prescriptions/{id}/approve")
  public void approvePrescription(@PathVariable("id") Long id) {
    adminService.approvePrescription(id);
  }

  /**
   * Rejects a specific user prescription upload.
   *
   * @param id the ID of the prescription to reject
   * @param req the request containing the rejection reason
   */
  @PutMapping("/prescriptions/{id}/reject")
  public void rejectPrescription(
      @PathVariable("id") Long id,
      @RequestBody CatalogAdminClient.RejectRequestDto req
  ) {
    adminService.rejectPrescription(id, req);
  }
}
