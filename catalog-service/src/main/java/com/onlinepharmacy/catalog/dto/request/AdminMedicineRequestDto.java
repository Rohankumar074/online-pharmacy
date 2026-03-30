package com.onlinepharmacy.catalog.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AdminMedicineRequestDto {

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Category ID is required")
  private Long categoryId;

  private String manufacturer;
  private String dosageForm;
  private String strength;
  private String imageUrl;
  private String status;

  @NotNull(message = "Price is required")
  @Positive(message = "Price must be positive")
  private BigDecimal price;

  @Min(value = 0, message = "Stock cannot be negative")
  private int stock;

  private boolean requiresPrescription;

  // --- Getters & Setters ---

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Long getCategoryId() { return categoryId; }
  public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

  public String getManufacturer() { return manufacturer; }
  public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

  public String getDosageForm() { return dosageForm; }
  public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }

  public String getStrength() { return strength; }
  public void setStrength(String strength) { this.strength = strength; }

  public String getImageUrl() { return imageUrl; }
  public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }

  public int getStock() { return stock; }
  public void setStock(int stock) { this.stock = stock; }

  public boolean isRequiresPrescription() { return requiresPrescription; }
  public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }
}
