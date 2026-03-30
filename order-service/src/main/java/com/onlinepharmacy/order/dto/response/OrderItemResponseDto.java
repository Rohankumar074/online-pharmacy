package com.onlinepharmacy.order.dto.response;

import java.math.BigDecimal;

public record OrderItemResponseDto(Long medicineId, String medicineName, int quantity, BigDecimal unitPrice) {}
