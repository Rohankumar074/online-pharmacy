package com.onlinepharmacy.catalog.service;

import com.onlinepharmacy.catalog.exception.ResourceNotFoundException;
import com.onlinepharmacy.catalog.model.Category;
import com.onlinepharmacy.catalog.repo.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {

  private CategoryService categoryService;

  @Mock
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    categoryService = new CategoryService(categoryRepository);
  }

  @Test
  public void updateCategory_whenExists_updatesFields() {
    Category existing = new Category();
    existing.setId(1L);
    existing.setName("Old");

    when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

    Category detail = new Category();
    detail.setName("New");
    detail.setDescription("Desc");

    Category result = categoryService.updateCategory(1L, detail);

    Assertions.assertEquals("New", result.getName());
    Assertions.assertEquals("Desc", result.getDescription());
  }

  @Test
  public void updateCategory_whenNotExists_throws() {
    when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
    Assertions.assertThrows(ResourceNotFoundException.class, () -> 
        categoryService.updateCategory(99L, new Category()));
  }
}
