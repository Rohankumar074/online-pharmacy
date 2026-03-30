package com.onlinepharmacy.catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "cat_prescriptions")
public class Prescription {

  public enum Status {
    PENDING,
    APPROVED,
    REJECTED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String customerId;

  @Column(nullable = false)
  private Long orderId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  /**
   * Scaffold: store filename only.
   * In production, store object storage key (S3/GCS) or filesystem path.
   */
  @Column(length = 500)
  private String filename;

  private Instant updatedAt;

  @PrePersist
  protected void onCreate() {
    if (this.createdAt == null) {
        this.createdAt = Instant.now();
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public Prescription() {
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

  public Long getOrderId() {
	return orderId;
  }
 
  public void setOrderId(Long orderId) {
	this.orderId = orderId;
  }

  public Status getStatus() {
	return status;
  }

  public void setStatus(Status status) {
	this.status = status;
  }

  public Instant getCreatedAt() {
	return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
	this.createdAt = createdAt;
  }

  public String getFilename() {
	return filename;
  }

  public void setFilename(String filename) {
	this.filename = filename;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}

