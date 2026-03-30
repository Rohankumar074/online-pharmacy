package com.onlinepharmacy.catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "cat_medicine_batches")
public class Batch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "medicine_id", nullable = false)
  private Medicine medicine;

  @Column(nullable = false, length = 50)
  private String batchNumber;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private LocalDate expiryDate;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  private Instant updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public Batch() {}

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Medicine getMedicine() { return medicine; }
  public void setMedicine(Medicine medicine) { this.medicine = medicine; }

  public String getBatchNumber() { return batchNumber; }
  public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }

  public LocalDate getExpiryDate() { return expiryDate; }
  public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
