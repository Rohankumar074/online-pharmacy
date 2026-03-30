package com.onlinepharmacy.order.dto.request;

import jakarta.validation.constraints.NotNull;

public class PaymentInitiateRequestDto {
  @NotNull(message = "Order ID is required")
  private Long orderId;

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}

