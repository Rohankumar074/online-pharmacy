package com.onlinepharmacy.catalog.repo;

import com.onlinepharmacy.catalog.model.Medicine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
  List<Medicine> findByNameContainingIgnoreCase(String name);
  List<Medicine> findByCategoryId(Long categoryId);
  List<Medicine> findByCategoryNameIgnoreCase(String categoryName);
  long countByStockLessThan(int threshold);
}
