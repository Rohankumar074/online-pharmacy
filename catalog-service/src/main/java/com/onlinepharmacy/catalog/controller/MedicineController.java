package com.onlinepharmacy.catalog.controller;

import com.onlinepharmacy.catalog.dto.response.MedicineResponseDto;
import com.onlinepharmacy.catalog.service.MedicineService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for retrieving medicine catalog information.
 */
@RestController
@RequestMapping("/api/catalog")
public class MedicineController {

  private final MedicineService medicineService;

  public MedicineController(MedicineService medicineService) {
    this.medicineService = medicineService;
  }

  /**
   * Lists medicines from the catalog, optionally filtering by name or category.
   *
   * @param name the medicine name to filter by (optional)
   * @param category the category name to filter by (optional)
   * @return a list of medicine responses
   */
  @GetMapping("/medicines")
  public List<MedicineResponseDto> listMedicines(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String category
  ) {
    return medicineService.listMedicines(name, category);
  }

  /**
   * Retrieves specific medicine details by its ID.
   *
   * @param id the ID of the medicine
   * @return the medicine details response
   */
  @GetMapping("/medicines/{id}")
  public MedicineResponseDto getMedicine(@PathVariable Long id) {
    return medicineService.getMedicine(id);
  }
}
