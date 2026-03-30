package com.onlinepharmacy.order.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ord_orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String customerId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private OrderStatus status = OrderStatus.DRAFT_CART;

  @Embedded
  private Address address;

  @Column(nullable = false, precision = 14, scale = 2)
  private BigDecimal totalAmount = BigDecimal.ZERO;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  private Instant updatedAt;

  @Column(nullable = false)
  private boolean substitutionEnabled = false;


  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public void recalculateTotal() {
    this.totalAmount = items.stream()
        .map(i -> {
            BigDecimal base = i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()));
            BigDecimal disc = i.getDiscount() != null ? i.getDiscount() : BigDecimal.ZERO;
            return base.subtract(disc);
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Order() {
	super();
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
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

  public Address getAddress() {
	return address;
  }

  public void setAddress(Address address) {
	this.address = address;
  }

  public BigDecimal getTotalAmount() {
	return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
	this.totalAmount = totalAmount;
  }

  public Instant getCreatedAt() {
	return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
	this.createdAt = createdAt;
  }

  public List<OrderItem> getItems() {
	return items;
  }

  public void setItems(List<OrderItem> items) {
	this.items = items;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public boolean isSubstitutionEnabled() {
    return substitutionEnabled;
  }

  public void setSubstitutionEnabled(boolean substitutionEnabled) {
    this.substitutionEnabled = substitutionEnabled;
  }
}



