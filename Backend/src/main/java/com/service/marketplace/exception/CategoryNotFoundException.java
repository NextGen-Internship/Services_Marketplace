package com.service.marketplace.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Integer categoryId) {
        super("Category not found with id: " + categoryId);
    }

    public CategoryNotFoundException() {
        super("Category not found!");
    }
}
