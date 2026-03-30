package com.onlinepharmacy.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "catalog-service",
    fallback = CatalogClientFallback.class
)
public interface CatalogClient {

  @GetMapping("/api/catalog/medicines/{id}")
  CatalogMedicineDto getMedicine(@PathVariable("id") Long id);

  record CatalogMedicineDto(
      Long id,
      String name,
      String category,
      java.math.BigDecimal price,
      boolean requiresPrescription,
      int stock
  ) {}
}
