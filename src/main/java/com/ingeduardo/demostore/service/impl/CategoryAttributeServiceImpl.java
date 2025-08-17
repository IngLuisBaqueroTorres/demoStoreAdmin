// CategoryAttributeServiceImpl.java
package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.*;
import com.ingeduardo.demostore.model.*;
import com.ingeduardo.demostore.repository.*;
import com.ingeduardo.demostore.service.CategoryAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryAttributeServiceImpl implements CategoryAttributeService {

    private final CategoryAttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryAttributeResponseDto createAttribute(CategoryAttributeRequestDto dto) {
        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        var attr = CategoryAttribute.builder()
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .category(category)
                .build();

        attr = attributeRepository.save(attr);
        return map(attr);
    }

    @Override
    public List<CategoryAttributeResponseDto> getAttributesByCategory(String categoryId) {
        return attributeRepository.findByCategoryId(categoryId).stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public CategoryAttributeResponseDto updateAttribute(String id, CategoryAttributeRequestDto dto) {
        var attr = attributeRepository.findById(id).orElseThrow(() -> new RuntimeException("Attribute not found"));
        if (dto.getName() != null) attr.setName(dto.getName());
        if (dto.getType() != null) attr.setType(dto.getType());
        if (dto.getDescription() != null) attr.setDescription(dto.getDescription());
        attr = attributeRepository.save(attr);
        return map(attr);
    }

    @Override
    public void deleteAttribute(String id) {
        attributeRepository.deleteById(id);
    }

    private CategoryAttributeResponseDto map(CategoryAttribute a) {
        return CategoryAttributeResponseDto.builder()
                .id(a.getId())
                .name(a.getName())
                .type(a.getType())
                .description(a.getDescription())
                .categoryId(a.getCategory() != null ? a.getCategory().getId() : null)
                .build();
    }
}
