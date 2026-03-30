package com.onlinepharmacy.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ord_order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(nullable = false)
  private Long medicineId;

  @Column(nullable = false)
  private String medicineName;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false, precision = 14, scale = 2)
  private BigDecimal unitPrice;

  @Column(precision = 14, scale = 2)
  private BigDecimal discount = BigDecimal.ZERO;

  public OrderItem() {
	super();
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public Order getOrder() {
	return order;
  }

  public void setOrder(Order order) {
	this.order = order;
  }

  public Long getMedicineId() {
	return medicineId;
  }

  public void setMedicineId(Long medicineId) {
	this.medicineId = medicineId;
  }

  public String getMedicineName() {
	return medicineName;
  }

  public void setMedicineName(String medicineName) {
	this.medicineName = medicineName;
  }

  public int getQuantity() {
	return quantity;
  }

  public void setQuantity(int quantity) {
	this.quantity = quantity;
  }

  public BigDecimal getUnitPrice() {
	return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
	this.unitPrice = unitPrice;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }
}

