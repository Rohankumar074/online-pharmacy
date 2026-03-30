package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.client.OrderInternalClient;
import com.onlinepharmacy.catalog.dto.response.PrescriptionUploadResponseDto;
import com.onlinepharmacy.catalog.model.Prescription;
import com.onlinepharmacy.catalog.repo.PrescriptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PrescriptionServiceTest {

  private PrescriptionService prescriptionService;

  @Mock
  private PrescriptionRepository prescriptionRepository;

  @Mock
  private OrderInternalClient orderInternalClient;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    prescriptionService = new PrescriptionService(prescriptionRepository, orderInternalClient);
  }

  @Test
  public void uploadPrescription_withValidData_savesAndReturns() {
    MultipartFile file = Mockito.mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("rx.png");

    Prescription pres = new Prescription();
    pres.setId(10L);
    pres.setOrderId(100L);
    pres.setStatus(Prescription.Status.PENDING);
    when(prescriptionRepository.save(any(Prescription.class))).thenReturn(pres);

    PrescriptionUploadResponseDto result = prescriptionService.uploadPrescription("user123", 100L, file);
    Assertions.assertEquals(Prescription.Status.PENDING, result.status());
  }

  @Test
  public void adminApprovePrescription_whenExists_updatesStatus() {
    Prescription pres = new Prescription();
    pres.setId(1L);
    pres.setCustomerId("user123");
    pres.setOrderId(100L);
    pres.setStatus(Prescription.Status.PENDING);

    when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(pres));

    PrescriptionUploadResponseDto result = prescriptionService.adminApprovePrescription(1L);
    Assertions.assertEquals(Prescription.Status.APPROVED, result.status());
    Mockito.verify(prescriptionRepository).save(pres);
  }

  @Test
  public void adminRejectPrescription_whenExists_updatesStatus() {
    Prescription pres = new Prescription();
    pres.setId(1L);
    pres.setCustomerId("user123");
    pres.setOrderId(100L);
    pres.setStatus(Prescription.Status.PENDING);

    when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(pres));

    PrescriptionUploadResponseDto result = prescriptionService.adminRejectPrescription(1L, "invalid");
    Assertions.assertEquals(Prescription.Status.REJECTED, result.status());
    Mockito.verify(prescriptionRepository).save(pres);
  }

  @Test
  public void getPrescriptionVolume_returnsMap() {
    when(prescriptionRepository.countByStatus(Prescription.Status.PENDING)).thenReturn(10L);
    when(prescriptionRepository.countByStatus(Prescription.Status.APPROVED)).thenReturn(5L);
    when(prescriptionRepository.countByStatus(Prescription.Status.REJECTED)).thenReturn(2L);

    java.util.Map<String, Long> result = prescriptionService.getPrescriptionVolume();

    Assertions.assertEquals(10L, result.get("PENDING"));
    Assertions.assertEquals(5L, result.get("APPROVED"));
    Assertions.assertEquals(2L, result.get("REJECTED"));
  }
}
