package com.onlinepharmacy.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class Address {

  @NotBlank
  @Column(length = 200)
  private String line1;

  @NotBlank
  @Column(length = 100)
  private String city;

  @NotBlank
  @Column(length = 20)
  private String pincode;

  @NotBlank
  @Column(length = 100)
  private String state;

  public String getLine1() {
    return line1;
  }

  public void setLine1(String line1) {
    this.line1 = line1;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}

