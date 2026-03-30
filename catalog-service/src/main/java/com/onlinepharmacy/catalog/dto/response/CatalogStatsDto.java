package com.onlinepharmacy.catalog.dto.response;

public record CatalogStatsDto(long pendingPrescriptions, long lowStockCount, long expiringBatchesCount) {}
