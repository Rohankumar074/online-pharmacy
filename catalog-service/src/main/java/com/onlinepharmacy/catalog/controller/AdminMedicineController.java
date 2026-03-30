package com.onlinepharmacy.catalog.controller;

import com.onlinepharmacy.catalog.dto.request.AdminMedicineRequestDto;
import com.onlinepharmacy.catalog.dto.request.RejectRequestDto;
import com.onlinepharmacy.catalog.dto.response.CatalogStatsDto;
import com.onlinepharmacy.catalog.dto.response.MedicineResponseDto;
import com.onlinepharmacy.catalog.dto.response.PrescriptionUploadResponseDto;
import com.onlinepharmacy.catalog.service.MedicineService;
import com.onlinepharmacy.catalog.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Internal REST Controller for administrative catalog operations, including medicine and prescription management.
 */
@RestController
@RequestMapping("/api/internal/admin")
public class AdminMedicineController {

  private final MedicineService medicineService;
  private final PrescriptionService prescriptionService;

  public AdminMedicineController(MedicineService medicineService, PrescriptionService prescriptionService) {
    this.medicineService = medicineService;
    this.prescriptionService = prescriptionService;
  }

  /**
   * Creates a new medicine in the catalog.
   *
   * @param req the medicine creation request details
   * @return the created medicine response
   */
  @PostMapping("/medicines")
  public MedicineResponseDto adminCreateMedicine(@Valid @RequestBody AdminMedicineRequestDto req) {
    return medicineService.adminCreateMedicine(req);
  }

  /**
   * Approves a specific uploaded prescription.
   *
   * @param id the ID of the prescription to approve
   * @return the updated prescription upload response
   */
  @PutMapping("/prescriptions/{id}/approve")
  public PrescriptionUploadResponseDto adminApprovePrescription(@PathVariable Long id) {
    return prescriptionService.adminApprovePrescription(id);
  }

  /**
   * Rejects a specific uploaded prescription with a valid reason.
   *
   * @param id the ID of the prescription to reject
   * @param req the request containing the rejection reason
   * @return the updated prescription upload response
   */
  @PutMapping("/prescriptions/{id}/reject")
  public PrescriptionUploadResponseDto adminRejectPrescription(
      @PathVariable Long id,
      @Valid @RequestBody RejectRequestDto req
  ) {
    return prescriptionService.adminRejectPrescription(id, req.getReason());
  }

  /**
   * Retrieves overall catalog statistics.
   *
   * @return the catalog statistics response
   */
  @GetMapping("/stats")
  public CatalogStatsDto getCatalogStats() {
    return medicineService.getCatalogStats();
  }
}
