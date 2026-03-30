package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.dto.request.AdminMedicineRequestDto;
import com.onlinepharmacy.catalog.dto.response.CatalogStatsDto;
import com.onlinepharmacy.catalog.dto.response.MedicineResponseDto;
import com.onlinepharmacy.catalog.exception.ResourceNotFoundException;
import com.onlinepharmacy.catalog.mapper.CatalogMapper;
import com.onlinepharmacy.catalog.model.Category;
import com.onlinepharmacy.catalog.model.Medicine;
import com.onlinepharmacy.catalog.model.Prescription;
import com.onlinepharmacy.catalog.repo.BatchRepository;
import com.onlinepharmacy.catalog.repo.CategoryRepository;
import com.onlinepharmacy.catalog.repo.MedicineRepository;
import com.onlinepharmacy.catalog.repo.PrescriptionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicineService {

  private final MedicineRepository medicineRepository;
  private final CategoryRepository categoryRepository;
  private final PrescriptionRepository prescriptionRepository;
  private final BatchRepository batchRepository;

  public MedicineService(
      MedicineRepository medicineRepository,
      CategoryRepository categoryRepository,
      PrescriptionRepository prescriptionRepository,
      BatchRepository batchRepository
  ) {
    this.medicineRepository = medicineRepository;
    this.categoryRepository = categoryRepository;
    this.prescriptionRepository = prescriptionRepository;
    this.batchRepository = batchRepository;
  }

  public List<MedicineResponseDto> listMedicines(String name, String category) {
    List<Medicine> meds;
    if (name != null && !name.isBlank()) {
      meds = medicineRepository.findByNameContainingIgnoreCase(name);
    } else if (category != null && !category.isBlank()) {
      meds = medicineRepository.findByCategoryNameIgnoreCase(category);
    } else {
      meds = medicineRepository.findAll();
    }
    return meds.stream().map(CatalogMapper::toMedicineResponseDto).toList();
  }

  public MedicineResponseDto getMedicine(Long id) {
    Medicine med = medicineRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
    return CatalogMapper.toMedicineResponseDto(med);
  }


  @Transactional
  public MedicineResponseDto adminCreateMedicine(AdminMedicineRequestDto req) {
    Category category = categoryRepository.findById(req.getCategoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + req.getCategoryId()));

    Medicine med = CatalogMapper.toEntity(req, category);
    med = medicineRepository.save(med);
    return CatalogMapper.toMedicineResponseDto(med);
  }

  public CatalogStatsDto getCatalogStats() {
    long pendingPrescriptions = prescriptionRepository.countByStatus(Prescription.Status.PENDING);
    long lowStockCount = medicineRepository.countByStockLessThan(10);
    long expiringBatchesCount = batchRepository.countByExpiryDateBefore(LocalDate.now().plusMonths(3));
    return new CatalogStatsDto(pendingPrescriptions, lowStockCount, expiringBatchesCount);
  }
}
