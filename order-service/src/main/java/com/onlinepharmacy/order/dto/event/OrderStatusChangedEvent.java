package com.onlinepharmacy.order.dto.event;

public record OrderStatusChangedEvent(Long orderId, String customerId, String newStatus) {}
