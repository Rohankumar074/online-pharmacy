package com.onlinepharmacy.notification.dto.event;

public record OrderStatusChangedEvent(Long orderId, String customerId, String newStatus) {}
