package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.model.Batch;
import com.onlinepharmacy.catalog.model.Medicine;
import com.onlinepharmacy.catalog.repo.BatchRepository;
import com.onlinepharmacy.catalog.repo.MedicineRepository;
import com.onlinepharmacy.catalog.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchService {
  private final BatchRepository batchRepository;
  private final MedicineRepository medicineRepository;

  public BatchService(BatchRepository batchRepository, MedicineRepository medicineRepository) {
    this.batchRepository = batchRepository;
    this.medicineRepository = medicineRepository;
  }

  public List<Batch> getAllBatches() {
    return batchRepository.findAll();
  }

  public List<Batch> getBatchesByMedicine(Long medicineId) {
    return batchRepository.findByMedicineId(medicineId);
  }

  @Transactional
  public Batch createBatch(Long medicineId, Batch batch) {
    Medicine med = medicineRepository.findById(medicineId)
        .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
    batch.setMedicine(med);
    
    // Update total stock of medicine
    med.setStock(med.getStock() + batch.getQuantity());
    medicineRepository.save(med);
    
    return batchRepository.save(batch);
  }

  @Transactional
  public Batch updateBatch(Long id, Batch batchDetails) {
    Batch batch = batchRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
    
    Medicine med = batch.getMedicine();
    int diff = batchDetails.getQuantity() - batch.getQuantity();
    med.setStock(med.getStock() + diff);
    medicineRepository.save(med);
    
    batch.setBatchNumber(batchDetails.getBatchNumber());
    batch.setQuantity(batchDetails.getQuantity());
    batch.setExpiryDate(batchDetails.getExpiryDate());
    return batchRepository.save(batch);
  }

  @Transactional
  public void deleteBatch(Long id) {
    Batch batch = batchRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
    
    Medicine med = batch.getMedicine();
    med.setStock(med.getStock() - batch.getQuantity());
    medicineRepository.save(med);
    
    batchRepository.deleteById(id);
  }

  public List<Batch> getExpiringBatches() {
    return batchRepository.findByExpiryDateBefore(java.time.LocalDate.now().plusMonths(3));
  }
}
