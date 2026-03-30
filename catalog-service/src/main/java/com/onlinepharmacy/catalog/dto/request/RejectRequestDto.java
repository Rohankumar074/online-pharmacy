package com.onlinepharmacy.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RejectRequestDto {
  @NotBlank(message = "Reason is required")
  private String reason;

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}

