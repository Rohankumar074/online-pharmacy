package com.onlinepharmacy.catalog.repo;

import com.onlinepharmacy.catalog.model.Prescription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
  List<Prescription> findTop10ByCustomerIdOrderByCreatedAtDesc(String customerId);
  long countByStatus(Prescription.Status status);
}

