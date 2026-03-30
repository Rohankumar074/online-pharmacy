package com.onlinepharmacy.order.client;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class CatalogClientFallback implements CatalogClient {

  @Override
  public CatalogMedicineDto getMedicine(Long id) {
    // Return dummy DTO with 0 stock to trigger Out of Stock validation naturally
    return new CatalogMedicineDto(
        id,
        "Unknown",
        "Unknown",
        BigDecimal.ZERO,
        false,
        0
    );
  }
}
