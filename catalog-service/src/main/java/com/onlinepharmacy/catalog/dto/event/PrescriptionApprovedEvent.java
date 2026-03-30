package com.onlinepharmacy.catalog.dto.event;

public record PrescriptionApprovedEvent(Long prescriptionId, String customerId, Long medicineId) {
}

