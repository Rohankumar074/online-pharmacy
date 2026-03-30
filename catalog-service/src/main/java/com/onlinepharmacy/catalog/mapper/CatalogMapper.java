package com.onlinepharmacy.catalog.mapper;

import com.onlinepharmacy.catalog.dto.response.MedicineResponseDto;
import com.onlinepharmacy.catalog.model.Medicine;

import com.onlinepharmacy.catalog.dto.request.AdminMedicineRequestDto;
import com.onlinepharmacy.catalog.model.Category;

public final class CatalogMapper {

  private CatalogMapper() {}

  public static Medicine toEntity(AdminMedicineRequestDto req, Category category) {
    Medicine med = new Medicine();
    med.setName(req.getName());
    med.setCategory(category);
    med.setManufacturer(req.getManufacturer());
    med.setDosageForm(req.getDosageForm());
    med.setStrength(req.getStrength());
    med.setImageUrl(req.getImageUrl());
    med.setStatus(req.getStatus() != null ? Medicine.Status.valueOf(req.getStatus()) : Medicine.Status.ACTIVE);
    med.setPrice(req.getPrice());
    med.setStock(req.getStock());
    med.setRequiresPrescription(req.isRequiresPrescription());
    return med;
  }

  public static MedicineResponseDto toMedicineResponseDto(Medicine med) {
    return new MedicineResponseDto(
        med.getId(),
        med.getName(),
        med.getCategory() != null ? med.getCategory().getName() : null,
        med.getManufacturer(),
        med.getDosageForm(),
        med.getStrength(),
        med.getImageUrl(),
        med.getStatus() != null ? med.getStatus().name() : null,
        med.getPrice(),
        med.isRequiresPrescription(),
        med.getStock(),
        med.getCreatedAt(),
        med.getUpdatedAt()
    );
  }
}
