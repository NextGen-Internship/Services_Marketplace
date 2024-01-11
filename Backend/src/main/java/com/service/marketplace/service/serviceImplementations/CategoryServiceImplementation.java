package com.service.marketplace.service.serviceImplementations;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.dto.response.CategoryResponse;
import com.service.marketplace.mapper.CategoryMapper;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.repository.CategoryRepository;
import com.service.marketplace.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImplementation implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        return category;
    }

    @Override
    public Category createCategory(CategoryRequest categoryToCreate) {
        Category newCategory = categoryMapper.categoryRequestToCategory(categoryToCreate);
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category updateCategory(Integer categoryId, CategoryRequest categoryToUpdate) {
        Category existingCategory = categoryRepository.findById(categoryId).orElse(null);

        if (existingCategory != null) {
//            existingCategory.setName(categoryToUpdate.getName());
//            existingCategory.setDescription(categoryToUpdate.getDescription());
            existingCategory = categoryMapper.categoryRequestToCategory(categoryToUpdate);

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
