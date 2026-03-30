package com.onlinepharmacy.order.dto.response;

import com.onlinepharmacy.order.model.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

public record OrderCartResponseDto(Long id, OrderStatus status, List<OrderItemResponseDto> items, BigDecimal totalAmount) {}
