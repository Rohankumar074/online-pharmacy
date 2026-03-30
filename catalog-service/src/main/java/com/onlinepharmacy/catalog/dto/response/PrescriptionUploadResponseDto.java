package com.onlinepharmacy.catalog.dto.response;

import com.onlinepharmacy.catalog.model.Prescription;

public record PrescriptionUploadResponseDto(Long prescriptionId, Prescription.Status status) {}
