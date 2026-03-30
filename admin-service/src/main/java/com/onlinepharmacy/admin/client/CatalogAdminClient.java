package com.onlinepharmacy.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@FeignClient(
    name = "catalog-service",
    fallback = CatalogAdminClientFallback.class
)
public interface CatalogAdminClient {

  @GetMapping("/api/catalog/medicines")
  java.util.List<MedicineDto> listMedicines();

  @PostMapping("/api/internal/admin/medicines")
  MedicineDto createMedicine(@RequestBody AdminMedicineRequestDto req);

  @org.springframework.web.bind.annotation.PutMapping("/api/internal/admin/prescriptions/{id}/approve")
  void approvePrescription(@PathVariable("id") Long id);

  @org.springframework.web.bind.annotation.PutMapping("/api/internal/admin/prescriptions/{id}/reject")
  void rejectPrescription(@PathVariable("id") Long id, @RequestBody RejectRequestDto req);

  @GetMapping("/api/internal/admin/stats")
  CatalogStatsDto getCatalogStats();

  @GetMapping("/api/internal/admin/reports/prescriptions/volume")
  java.util.Map<String, Long> getPrescriptionVolume();

  @GetMapping("/api/internal/admin/reports/batches/expiring")
  java.util.List<BatchDto> getExpiringBatches();

  record RejectRequestDto(String reason) {}

  record CatalogStatsDto(long pendingPrescriptions, long lowStockCount, long expiringBatchesCount) {}

  record AdminDashboard(long lowStockCount, long totalMedicines, long pendingPrescriptions, long expiringBatches, long todayOrders) {}

  record MedicineDto(
      Long id,
      String name,
      String category,
      String manufacturer,
      String dosageForm,
      String strength,
      String imageUrl,
      String status,
      BigDecimal price,
      boolean requiresPrescription,
      int stock,
      Instant createdAt,
      Instant updatedAt
  ) {}

  record AdminMedicineRequestDto(
      @NotBlank(message = "Name is required") String name,
      @NotNull(message = "Category ID is required") Long categoryId,
      String manufacturer,
      String dosageForm,
      String strength,
      String imageUrl,
      String status,
      @NotNull(message = "Price is required") @Positive(message = "Price must be positive") BigDecimal price,
      @Min(value = 0, message = "Stock cannot be negative") int stock,
      boolean requiresPrescription
  ) {}

  record BatchDto(
      Long id,
      String batchNumber,
      int quantity,
      java.time.LocalDate expiryDate,
      Instant createdAt,
      Instant updatedAt
  ) {}
}
