package com.onlinepharmacy.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateCartItemRequestDto {
  @NotNull
  private Long medicineId;
  @Min(0)
  private int quantity;

  public UpdateCartItemRequestDto() {}

  public UpdateCartItemRequestDto(Long medicineId, int quantity) {
    this.medicineId = medicineId;
    this.quantity = quantity;
  }

  public Long getMedicineId() {
    return medicineId;
  }

  public void setMedicineId(Long medicineId) {
    this.medicineId = medicineId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
