package com.onlinepharmacy.catalog.repo;

import com.onlinepharmacy.catalog.model.Batch;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
  List<Batch> findByMedicineId(Long medicineId);
  List<Batch> findByExpiryDateBefore(LocalDate date);
  long countByExpiryDateBefore(LocalDate date);
}
