package com.onlinepharmacy.catalog.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record MedicineResponseDto(
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
