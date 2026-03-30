package com.onlinepharmacy.admin.dto.response;

public class SalesReportDto {
  private long orders;
  private double totalRevenue;
  private String message;

  public SalesReportDto() {
  }

  public SalesReportDto(long orders, double totalRevenue, String message) {
    this.orders = orders;
    this.totalRevenue = totalRevenue;
    this.message = message;
  }

  public long getOrders() {
    return orders;
  }

  public void setOrders(long orders) {
    this.orders = orders;
  }

  public double getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(double totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
