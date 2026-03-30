package com.onlinepharmacy.catalog.dto.event;

public record PrescriptionUploadedEvent(Long prescriptionId, String customerId, Long medicineId) {
}

