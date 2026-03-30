package com.onlinepharmacy.catalog.controller;

import com.onlinepharmacy.catalog.model.Batch;
import com.onlinepharmacy.catalog.service.BatchService;
import com.onlinepharmacy.catalog.service.PrescriptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * Internal REST Controller for generating catalog-related reports.
 */
@RestController
@RequestMapping("/api/internal/admin/reports")
public class AdminReportController {

  private final PrescriptionService prescriptionService;
  private final BatchService batchService;

  public AdminReportController(PrescriptionService prescriptionService, BatchService batchService) {
    this.prescriptionService = prescriptionService;
    this.batchService = batchService;
  }

  /**
   * Generates a report showing the volume of uploaded prescriptions.
   *
   * @return a map containing prescription volume statistics
   */
  @GetMapping("/prescriptions/volume")
  public Map<String, Long> getPrescriptionVolume() {
    return prescriptionService.getPrescriptionVolume();
  }

  /**
   * Retrieves a list of expiring medicine batches for administrative review.
   *
   * @return a list of expiring batches
   */
  @GetMapping("/batches/expiring")
  public List<Batch> getExpiringBatches() {
    return batchService.getExpiringBatches();
  }
}
