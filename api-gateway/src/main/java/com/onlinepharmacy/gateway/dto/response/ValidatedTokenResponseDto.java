package com.onlinepharmacy.gateway.dto.response;

import java.util.List;

public class ValidatedTokenResponseDto {
  private String userId;
  private List<String> roles;

  public ValidatedTokenResponseDto() {
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }
}
