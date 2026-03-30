package com.onlinepharmacy.order.dto.response;

import com.onlinepharmacy.order.model.OrderStatus;

public record OrderPaymentResponseDto(Long orderId, OrderStatus status, String message) {}
