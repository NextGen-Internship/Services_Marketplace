package com.service.marketplace.service;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.persistence.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Integer categoryId);

    Category createCategory(CategoryRequest categoryToCreate);

    Category updateCategory(Integer categoryId, CategoryRequest categoryToUpdate);

    void deleteCategoryById(Integer categoryId);
}
