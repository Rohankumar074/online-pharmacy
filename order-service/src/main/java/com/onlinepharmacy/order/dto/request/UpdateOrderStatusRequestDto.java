package com.onlinepharmacy.order.dto.request;

import com.onlinepharmacy.order.model.OrderStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequestDto {
  @NotNull(message = "Status is required")
  private OrderStatus status;

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }
}

