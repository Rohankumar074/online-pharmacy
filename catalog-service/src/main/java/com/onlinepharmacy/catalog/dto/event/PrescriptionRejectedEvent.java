package com.onlinepharmacy.catalog.dto.event;

public record PrescriptionRejectedEvent(Long prescriptionId, String customerId, Long medicineId, String reason) {
}

