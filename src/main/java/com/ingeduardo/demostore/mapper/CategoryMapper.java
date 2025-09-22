package com.ingeduardo.demostore.mapper;

import com.ingeduardo.demostore.dto.CategoryRequestDto;
import com.ingeduardo.demostore.dto.CategoryResponseDto;
import com.ingeduardo.demostore.model.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public static CategoryResponseDto toResponseDto(Category entity) {
        if (entity == null) {
            return null;
        }
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
    
    public static void updateEntityFromDto(CategoryRequestDto dto, Category entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
