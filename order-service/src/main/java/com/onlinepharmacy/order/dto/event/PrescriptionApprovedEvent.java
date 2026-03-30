package com.onlinepharmacy.order.dto.event;

public record PrescriptionApprovedEvent(Long prescriptionId, String customerId, Long medicineId) {
}

