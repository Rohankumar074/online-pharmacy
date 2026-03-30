package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.dto.request.AdminMedicineRequestDto;
import com.onlinepharmacy.catalog.dto.response.CatalogStatsDto;
import com.onlinepharmacy.catalog.dto.response.MedicineResponseDto;
import com.onlinepharmacy.catalog.exception.ResourceNotFoundException;
import com.onlinepharmacy.catalog.model.Category;
import com.onlinepharmacy.catalog.model.Medicine;
import com.onlinepharmacy.catalog.model.Prescription;
import com.onlinepharmacy.catalog.repo.BatchRepository;
import com.onlinepharmacy.catalog.repo.CategoryRepository;
import com.onlinepharmacy.catalog.repo.MedicineRepository;
import com.onlinepharmacy.catalog.repo.PrescriptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MedicineServiceTest {

  private MedicineService medicineService;

  @Mock
  private MedicineRepository medicineRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private PrescriptionRepository prescriptionRepository;

  @Mock
  private BatchRepository batchRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    medicineService = new MedicineService(medicineRepository, categoryRepository, prescriptionRepository, batchRepository);
  }

  @Test
  public void listMedicines_withName_filtersByName() {
    when(medicineRepository.findByNameContainingIgnoreCase("paracetamol"))
        .thenReturn(List.of(new Medicine()));
    
    List<MedicineResponseDto> result = medicineService.listMedicines("paracetamol", null);
    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getMedicine_whenExists_returnsDto() {
    Medicine med = new Medicine();
    med.setId(1L);
    med.setName("Test");
    
    when(medicineRepository.findById(1L)).thenReturn(Optional.of(med));
    
    MedicineResponseDto result = medicineService.getMedicine(1L);
    Assertions.assertEquals("Test", result.name());
  }

  @Test
  public void getMedicine_whenNotExists_throws() {
    when(medicineRepository.findById(99L)).thenReturn(Optional.empty());
    Assertions.assertThrows(ResourceNotFoundException.class, () -> medicineService.getMedicine(99L));
  }

  @Test
  public void adminCreateMedicine_withValidCategory_savesAndReturns() {
    AdminMedicineRequestDto req = new AdminMedicineRequestDto();
    req.setName("New Med");
    req.setCategoryId(1L);

    Category cat = new Category();
    cat.setId(1L);

    when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
    when(medicineRepository.save(any(Medicine.class))).thenAnswer(i -> i.getArgument(0));

    MedicineResponseDto result = medicineService.adminCreateMedicine(req);
    Assertions.assertEquals("New Med", result.name());
  }

  @Test
  public void getCatalogStats_returnsSummary() {
    when(prescriptionRepository.countByStatus(Prescription.Status.PENDING)).thenReturn(5L);
    when(medicineRepository.countByStockLessThan(10)).thenReturn(3L);
    when(batchRepository.countByExpiryDateBefore(any())).thenReturn(2L);

    CatalogStatsDto stats = medicineService.getCatalogStats();
    Assertions.assertEquals(5L, stats.pendingPrescriptions());
    Assertions.assertEquals(3L, stats.lowStockCount());
    Assertions.assertEquals(2L, stats.expiringBatchesCount());
  }
}
