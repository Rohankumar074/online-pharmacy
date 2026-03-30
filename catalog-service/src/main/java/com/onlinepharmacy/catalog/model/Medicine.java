package com.onlinepharmacy.catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cat_medicines")
public class Medicine {

  public enum Status {
    ACTIVE, INACTIVE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 250)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(length = 200)
  private String manufacturer;

  @Column(length = 50)
  private String dosageForm;

  @Column(length = 50)
  private String strength;

  @Column(nullable = false, precision = 14, scale = 2)
  private BigDecimal price = BigDecimal.ZERO;

  @Column(nullable = false)
  private int stock = 0;

  @Column(nullable = false)
  private boolean requiresPrescription;

  @Column(length = 1000)
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Status status = Status.ACTIVE;

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

  public Medicine() {}

  // --- Getters & Setters ---

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Category getCategory() { return category; }
  public void setCategory(Category category) { this.category = category; }

  public String getManufacturer() { return manufacturer; }
  public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

  public String getDosageForm() { return dosageForm; }
  public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }

  public String getStrength() { return strength; }
  public void setStrength(String strength) { this.strength = strength; }

  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }

  public int getStock() { return stock; }
  public void setStock(int stock) { this.stock = stock; }

  public boolean isRequiresPrescription() { return requiresPrescription; }
  public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }

  public String getImageUrl() { return imageUrl; }
  public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
