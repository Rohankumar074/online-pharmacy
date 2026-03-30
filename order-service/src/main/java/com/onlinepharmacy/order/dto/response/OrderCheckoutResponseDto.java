package com.onlinepharmacy.order.dto.response;

import com.onlinepharmacy.order.model.OrderStatus;

public record OrderCheckoutResponseDto(Long orderId, OrderStatus status) {}
