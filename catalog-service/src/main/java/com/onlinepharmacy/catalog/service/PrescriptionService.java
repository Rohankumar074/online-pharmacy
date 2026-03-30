package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.dto.response.PrescriptionUploadResponseDto;
import com.onlinepharmacy.catalog.exception.ResourceNotFoundException;
import com.onlinepharmacy.catalog.exception.UnauthorizedException;
import com.onlinepharmacy.catalog.model.Prescription;
import com.onlinepharmacy.catalog.repo.PrescriptionRepository;
import com.onlinepharmacy.catalog.client.OrderInternalClient;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PrescriptionService {

  private final PrescriptionRepository prescriptionRepository;
  private final OrderInternalClient orderInternalClient;

  public PrescriptionService(
      PrescriptionRepository prescriptionRepository,
      OrderInternalClient orderInternalClient
  ) {
    this.prescriptionRepository = prescriptionRepository;
    this.orderInternalClient = orderInternalClient;
  }

  @Transactional
  public PrescriptionUploadResponseDto uploadPrescription(String customerId, Long orderId, MultipartFile file) {
    if (customerId == null || customerId.isBlank()) {
      throw new UnauthorizedException("Missing customer identity");
    }
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Prescription file is required");
    }

    // Note: We are now tying prescription to orderId. 
    // We could validate orderId here via order-service if needed, but for now we follow the user request to link to order.

    Prescription prescription = new Prescription();
    prescription.setCustomerId(customerId);
    prescription.setOrderId(orderId);
    prescription.setStatus(Prescription.Status.PENDING);
    prescription.setCreatedAt(Instant.now());
    prescription.setFilename(file.getOriginalFilename());
    prescription = prescriptionRepository.save(prescription);

    try {
      orderInternalClient.updatePrescriptionStatus(new OrderInternalClient.InternalPrescriptionUpdateDto(customerId, orderId, "PRESCRIPTION_PENDING"));
    } catch (Exception e) {
      org.slf4j.LoggerFactory.getLogger(PrescriptionService.class)
          .warn("Could not update order status (order-service unavailable?): {}", e.getMessage());
    }

    return new PrescriptionUploadResponseDto(prescription.getId(), prescription.getStatus());
  }

  @Transactional
  public PrescriptionUploadResponseDto adminApprovePrescription(Long id) {
    Prescription prescription = prescriptionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

    prescription.setStatus(Prescription.Status.APPROVED);
    prescriptionRepository.save(prescription);

    try {
      org.slf4j.LoggerFactory.getLogger(PrescriptionService.class)
          .info("Attempting to update order-service status for orderId: {} to {}", prescription.getOrderId(), "PRESCRIPTION_APPROVED");
      orderInternalClient.updatePrescriptionStatus(new OrderInternalClient.InternalPrescriptionUpdateDto(prescription.getCustomerId(), prescription.getOrderId(), "PRESCRIPTION_APPROVED"));
    } catch (Exception e) {
      org.slf4j.LoggerFactory.getLogger(PrescriptionService.class)
          .error("FAILED to update order status for approval. orderId: {}, error: {}", prescription.getOrderId(), e.getMessage(), e);
    }
    return new PrescriptionUploadResponseDto(prescription.getId(), prescription.getStatus());
  }

  @Transactional
  public PrescriptionUploadResponseDto adminRejectPrescription(Long id, String reason) {
    Prescription prescription = prescriptionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

    prescription.setStatus(Prescription.Status.REJECTED);
    prescriptionRepository.save(prescription);

    try {
      org.slf4j.LoggerFactory.getLogger(PrescriptionService.class)
          .info("Attempting to update order-service status for orderId: {} to {}", prescription.getOrderId(), "PRESCRIPTION_REJECTED");
      orderInternalClient.updatePrescriptionStatus(new OrderInternalClient.InternalPrescriptionUpdateDto(prescription.getCustomerId(), prescription.getOrderId(), "PRESCRIPTION_REJECTED"));
    } catch (Exception e) {
       org.slf4j.LoggerFactory.getLogger(PrescriptionService.class)
          .error("FAILED to update order status for rejection. orderId: {}, error: {}", prescription.getOrderId(), e.getMessage(), e);
    }
    return new PrescriptionUploadResponseDto(prescription.getId(), prescription.getStatus());
  }

  public java.util.Map<String, Long> getPrescriptionVolume() {
    return java.util.Map.of(
        "PENDING", prescriptionRepository.countByStatus(Prescription.Status.PENDING),
        "APPROVED", prescriptionRepository.countByStatus(Prescription.Status.APPROVED),
        "REJECTED", prescriptionRepository.countByStatus(Prescription.Status.REJECTED)
    );
  }
}
