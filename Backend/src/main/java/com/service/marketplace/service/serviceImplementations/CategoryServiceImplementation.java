package com.service.marketplace.service.serviceImplementations;

import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.repository.CategoryRepository;
import com.service.marketplace.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Category createCategory(Category categoryToCreate) {
        return categoryRepository.save(categoryToCreate);
    }

    @Override
    public Category updateCategory(Integer categoryId, Category categoryToUpdate) {
        Category existingCategory = categoryRepository.findById(categoryId).orElse(null);

        if (existingCategory != null) {
            existingCategory.setName(categoryToUpdate.getName());
            existingCategory.setDescription(categoryToUpdate.getDescription());

            return categoryRepository.save(existingCategory);
        } else {
            return null;
        }
    }

    @Override
    public void deleteCategoryById(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
