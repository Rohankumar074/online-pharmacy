package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.model.Category;
import com.onlinepharmacy.catalog.repo.CategoryRepository;
import com.onlinepharmacy.catalog.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Transactional
  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Transactional
  public Category updateCategory(Long id, Category categoryDetails) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    category.setName(categoryDetails.getName());
    category.setDescription(categoryDetails.getDescription());
    return categoryRepository.save(category);
  }

  @Transactional
  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }
}
