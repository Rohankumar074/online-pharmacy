package com.onlinepharmacy.identity.dto.response;

import java.util.List;

public class AuthResponseDto {
  private String accessToken;
  private List<String> roles;

  public AuthResponseDto() {
  }

  public AuthResponseDto(String accessToken, List<String> roles) {
    this.accessToken = accessToken;
    this.roles = roles;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }
}
