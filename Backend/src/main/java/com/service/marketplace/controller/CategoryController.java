package com.service.marketplace.controller;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Integer categoryId) {
        Category category = categoryService.getCategoryById(categoryId);

        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryToCreate) {
        Category newCategory = categoryService.createCategory(categoryToCreate);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Integer categoryId, @RequestBody CategoryRequest categoryToUpdate) {
        try {
            Category updatedCategory = categoryService.updateCategory(categoryId, categoryToUpdate);

            if (updatedCategory == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(updatedCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok().build();
    }
}
