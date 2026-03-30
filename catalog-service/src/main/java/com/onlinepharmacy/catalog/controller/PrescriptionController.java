package com.onlinepharmacy.catalog.controller;

import com.onlinepharmacy.catalog.dto.response.PrescriptionUploadResponseDto;
import com.onlinepharmacy.catalog.service.PrescriptionService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for managing prescription uploads.
 */
@RestController
@RequestMapping("/api/catalog")
public class PrescriptionController {

  private final PrescriptionService prescriptionService;

  public PrescriptionController(PrescriptionService prescriptionService) {
    this.prescriptionService = prescriptionService;
  }

  /**
   * Uploads a prescription file for a specific order.
   *
   * @param authentication the current user's authentication
   * @param orderId the ID of the order requiring the prescription
   * @param file the prescription file to upload
   * @return the prescription upload response details
   */
  @PostMapping(
      value = "/prescriptions/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public PrescriptionUploadResponseDto uploadPrescription(
      Authentication authentication,
      @RequestParam("orderId") Long orderId,
      @RequestPart("file") MultipartFile file
  ) {
    String customerId = authentication.getName();
    return prescriptionService.uploadPrescription(customerId, orderId, file);
  }
}
