package com.onlinepharmacy.admin.client;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CatalogAdminClientFallback implements CatalogAdminClient {

  @Override
  public List<MedicineDto> listMedicines() {
    return Collections.emptyList();
  }

  @Override
  public MedicineDto createMedicine(AdminMedicineRequestDto req) {
    // Return an error/placeholder fallback
    return new MedicineDto(
        -1L, req.name(), req.categoryId() != null ? String.valueOf(req.categoryId()) : "Unknown",
        req.manufacturer(), req.dosageForm(), req.strength(), req.imageUrl(),
        "CIRCUIT_OPEN", req.price(), req.requiresPrescription(), req.stock(), null, null
    );
  }

  @Override
  public void approvePrescription(Long id) {
    // Fallback logic
  }

  @Override
  public void rejectPrescription(Long id, RejectRequestDto req) {
    // Fallback logic
  }

  @Override
  public CatalogStatsDto getCatalogStats() {
    return new CatalogStatsDto(0, 0, 0);
  }

  @Override
  public java.util.Map<String, Long> getPrescriptionVolume() {
    return java.util.Collections.emptyMap();
  }

  @Override
  public List<BatchDto> getExpiringBatches() {
    return java.util.Collections.emptyList();
  }
}
