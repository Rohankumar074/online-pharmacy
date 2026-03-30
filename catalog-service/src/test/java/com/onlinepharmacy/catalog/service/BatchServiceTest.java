package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.model.Batch;
import com.onlinepharmacy.catalog.model.Medicine;
import com.onlinepharmacy.catalog.repo.BatchRepository;
import com.onlinepharmacy.catalog.repo.MedicineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BatchServiceTest {

  private BatchService batchService;

  @Mock
  private BatchRepository batchRepository;

  @Mock
  private MedicineRepository medicineRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    batchService = new BatchService(batchRepository, medicineRepository);
  }

  @Test
  public void createBatch_updatesMedicineStock() {
    Medicine med = new Medicine();
    med.setId(1L);
    med.setStock(10);
    when(medicineRepository.findById(1L)).thenReturn(Optional.of(med));

    Batch batch = new Batch();
    batch.setQuantity(5);
    when(batchRepository.save(any(Batch.class))).thenReturn(batch);

    Batch result = batchService.createBatch(1L, batch);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(15, med.getStock());
    verify(medicineRepository).save(med);
    verify(batchRepository).save(batch);
  }

  @Test
  public void updateBatch_adjustsStockDiff() {
    Medicine med = new Medicine();
    med.setStock(20);

    Batch existing = new Batch();
    existing.setQuantity(10);
    existing.setMedicine(med);
    when(batchRepository.findById(1L)).thenReturn(Optional.of(existing));

    Batch updated = new Batch();
    updated.setQuantity(15); // diff is +5

    batchService.updateBatch(1L, updated);

    Assertions.assertEquals(25, med.getStock());
    verify(medicineRepository).save(med);
  }

  @Test
  public void deleteBatch_decreasesStock() {
    Medicine med = new Medicine();
    med.setStock(20);

    Batch existing = new Batch();
    existing.setQuantity(10);
    existing.setMedicine(med);
    when(batchRepository.findById(1L)).thenReturn(Optional.of(existing));

    batchService.deleteBatch(1L);

    Assertions.assertEquals(10, med.getStock());
    verify(medicineRepository).save(med);
    verify(batchRepository).deleteById(1L);
  }
}
