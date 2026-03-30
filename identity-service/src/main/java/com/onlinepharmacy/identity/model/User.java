package com.onlinepharmacy.identity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "id_users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 200)
  private String email;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(nullable = false, length = 30)
  private String mobile;

  @Column(nullable = false, length = 200)
  private String passwordHash;

  /**
   * Comma-separated roles, e.g. "CUSTOMER" or "CUSTOMER,ADMIN".
   */
  @Column(nullable = false, length = 500)
  private String roles;

  @Column(nullable = false)
  private boolean enabled = true;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  private Instant updatedAt;

  private LocalDate dateOfBirth;

  @Column(length = 50)
  private String gender;

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public List<String> rolesAsList() {
    if (roles == null || roles.isBlank()) {
      return List.of();
    }
    return Arrays.stream(roles.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());
  }

  public User() {
	super();
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public String getEmail() {
	return email;
  }

  public void setEmail(String email) {
	this.email = email;
  }

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }

  public String getMobile() {
	return mobile;
  }

  public void setMobile(String mobile) {
	this.mobile = mobile;
  }

  public String getPasswordHash() {
	return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
  }

  public String getRoles() {
	return roles;
  }

  public void setRoles(String roles) {
	this.roles = roles;
  }

  public boolean isEnabled() {
	return enabled;
  }

  public void setEnabled(boolean enabled) {
	this.enabled = enabled;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }
}
