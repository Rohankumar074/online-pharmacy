package com.onlinepharmacy.order.dto.response;

import java.math.BigDecimal;

public record SalesReportResponseDto(
    long totalOrders,
    BigDecimal totalRevenue
) {}
