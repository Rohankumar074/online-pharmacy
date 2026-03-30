package com.onlinepharmacy.catalog.controller;

import com.onlinepharmacy.catalog.model.Category;
import com.onlinepharmacy.catalog.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Internal REST Controller for managing medicine categories.
 */
@RestController
@RequestMapping("/api/internal/admin")
public class AdminCategoryController {

  private final CategoryService categoryService;

  public AdminCategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * Retrieves all medicine categories.
   *
   * @return a list of all categories
   */
  @GetMapping("/categories")
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  /**
   * Creates a new medicine category.
   *
   * @param category the category details to create
   * @return the created category
   */
  @PostMapping("/categories")
  public Category createCategory(@RequestBody Category category) {
    return categoryService.createCategory(category);
  }

  /**
   * Updates an existing medicine category.
   *
   * @param id the ID of the category to update
   * @param category the updated category details
   * @return the updated category
   */
  @PutMapping("/categories/{id}")
  public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
    return categoryService.updateCategory(id, category);
  }

  /**
   * Deletes a specific category by its ID.
   *
   * @param id the ID of the category to delete
   */
  @DeleteMapping("/categories/{id}")
  public void deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
  }
}
