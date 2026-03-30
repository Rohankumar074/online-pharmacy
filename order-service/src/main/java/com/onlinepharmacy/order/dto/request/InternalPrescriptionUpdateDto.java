package com.onlinepharmacy.order.dto.request;

import com.onlinepharmacy.order.model.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InternalPrescriptionUpdateDto {
  @NotBlank
  private String customerId;
  @NotNull
  private OrderStatus status;
  @NotNull
  private Long orderId;

  public InternalPrescriptionUpdateDto() {}

  public InternalPrescriptionUpdateDto(String customerId, Long orderId, OrderStatus status) {
    this.customerId = customerId;
    this.orderId = orderId;
    this.status = status;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
