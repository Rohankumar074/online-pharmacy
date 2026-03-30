package com.onlinepharmacy.order.dto.response;

import com.onlinepharmacy.order.model.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import com.onlinepharmacy.order.dto.AddressDto;

public record OrderDetailsResponseDto(
    Long id,
    String customerId,
    OrderStatus status,
    AddressDto address,
    List<OrderItemResponseDto> items,
    BigDecimal totalAmount,
    Instant createdAt,
    Instant updatedAt
) {}
