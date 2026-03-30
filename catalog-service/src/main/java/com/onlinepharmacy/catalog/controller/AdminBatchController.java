package com.onlinepharmacy.catalog.controller;

import com.onlinepharmacy.catalog.model.Batch;
import com.onlinepharmacy.catalog.service.BatchService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Internal REST Controller for managing medicine batches.
 */
@RestController
@RequestMapping("/api/internal/admin")
public class AdminBatchController {

  private final BatchService batchService;

  public AdminBatchController(BatchService batchService) {
    this.batchService = batchService;
  }

  /**
   * Retrieves all medicine batches.
   *
   * @return a list of all batches
   */
  @GetMapping("/batches")
  public List<Batch> getAllBatches() {
    return batchService.getAllBatches();
  }

  /**
   * Retrieves all batches for a specific medicine.
   *
   * @param medicineId the ID of the medicine
   * @return a list of batches for the given medicine
   */
  @GetMapping("/medicines/{medicineId}/batches")
  public List<Batch> getBatchesByMedicine(@PathVariable Long medicineId) {
    return batchService.getBatchesByMedicine(medicineId);
  }

  /**
   * Creates a new batch for a specific medicine.
   *
   * @param medicineId the ID of the medicine
   * @param batch the batch details to create
   * @return the created batch
   */
  @PostMapping("/medicines/{medicineId}/batches")
  public Batch createBatch(@PathVariable Long medicineId, @RequestBody Batch batch) {
    return batchService.createBatch(medicineId, batch);
  }

  /**
   * Updates an existing batch.
   *
   * @param id the ID of the batch to update
   * @param batch the updated batch details
   * @return the updated batch
   */
  @PutMapping("/batches/{id}")
  public Batch updateBatch(@PathVariable Long id, @RequestBody Batch batch) {
    return batchService.updateBatch(id, batch);
  }

  /**
   * Deletes a specific batch by its ID.
   *
   * @param id the ID of the batch to delete
   */
  @DeleteMapping("/batches/{id}")
  public void deleteBatch(@PathVariable Long id) {
    batchService.deleteBatch(id);
  }
}
