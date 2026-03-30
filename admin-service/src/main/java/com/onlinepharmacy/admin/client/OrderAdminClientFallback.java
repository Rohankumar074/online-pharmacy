package com.onlinepharmacy.admin.client;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;

@Component
public class OrderAdminClientFallback implements OrderAdminClient {

  @Override
  public OrderDetailsDto updateOrderStatus(Long id, UpdateOrderStatusRequest req) {
    return new OrderDetailsDto(
        id, "Unknown", "CIRCUIT_OPEN", null, Collections.emptyList(), BigDecimal.ZERO, null, null
    );
  }

  @Override
  public SalesReportDto getSalesReport() {
    return new SalesReportDto(0L, BigDecimal.ZERO);
  }

  @Override
  public long getTodayOrderCount() {
    return 0;
  }
}
