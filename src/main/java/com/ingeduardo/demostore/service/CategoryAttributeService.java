package com.ingeduardo.demostore.service;

import java.util.List;

import com.ingeduardo.demostore.dto.CategoryAttributeRequestDto;
import com.ingeduardo.demostore.dto.CategoryAttributeResponseDto;

public interface CategoryAttributeService {
    CategoryAttributeResponseDto createAttribute(CategoryAttributeRequestDto dto);
    List<CategoryAttributeResponseDto> getAttributesByCategory(String categoryId);
    CategoryAttributeResponseDto updateAttribute(String id, CategoryAttributeRequestDto dto);
    void deleteAttribute(String id);
}
