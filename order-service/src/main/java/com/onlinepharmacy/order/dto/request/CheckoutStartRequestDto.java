package com.onlinepharmacy.order.dto.request;

import com.onlinepharmacy.order.dto.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CheckoutStartRequestDto {
  @NotNull(message = "Address is required")
  @Valid
  private AddressDto address;

  public AddressDto getAddress() {
    return address;
  }

  public void setAddress(AddressDto address) {
    this.address = address;
  }
}

