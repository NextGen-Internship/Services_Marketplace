package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.dto.response.CategoryResponse;
import com.service.marketplace.persistence.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryRequestToCategory(CategoryRequest request);

    CategoryResponse categoryToCategoryResponse(Category category);

    void categoryFromRequest(CategoryRequest request, @MappingTarget Category category);
}
