package com.service.marketplace.service;

import com.service.marketplace.persistence.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer categoryId);
    Category createCategory(Category categoryToCreate);
    Category updateCategory(Integer categoryId, Category categoryToUpdate);
    void deleteCategoryById(Integer categoryId);
}
