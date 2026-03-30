package com.onlinepharmacy.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

@FeignClient(
    name = "order-service",
    fallback = OrderAdminClientFallback.class
)
public interface OrderAdminClient {

  @PutMapping("/api/internal/admin/orders/{id}/status")
  OrderDetailsDto updateOrderStatus(
      @PathVariable("id") Long id,
      @RequestBody UpdateOrderStatusRequest req
  );

  @org.springframework.web.bind.annotation.GetMapping("/api/internal/admin/reports/sales")
  SalesReportDto getSalesReport();

  @org.springframework.web.bind.annotation.GetMapping("/api/internal/admin/stats/today-orders")
  long getTodayOrderCount();

  record SalesReportDto(long totalOrders, BigDecimal totalRevenue) {}

  record UpdateOrderStatusRequest(@NotBlank(message = "Status cannot be blank") String status) {}

  record OrderDetailsDto(
      Long id,
      String customerId,
      String status,
      AddressDto address,
      List<OrderItemDto> items,
      BigDecimal totalAmount,
      Instant createdAt,
      Instant updatedAt
  ) {}

  record AddressDto(String line1, String city, String pincode, String state) {}

  record OrderItemDto(Long medicineId, String medicineName, int quantity, BigDecimal unitPrice) {}
}

