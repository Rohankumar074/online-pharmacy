package com.onlinepharmacy.catalog.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")
public interface OrderInternalClient {

  @PostMapping("/api/internal/admin/orders/prescription-status")
  void updatePrescriptionStatus(@RequestBody InternalPrescriptionUpdateDto req);

  record InternalPrescriptionUpdateDto(String customerId, Long orderId, String status) {}
}
