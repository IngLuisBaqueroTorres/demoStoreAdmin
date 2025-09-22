package com.ingeduardo.demostore.service;

import java.util.List;

import com.ingeduardo.demostore.dto.CategoryAttributeValueRequestDto;
import com.ingeduardo.demostore.model.CategoryAttributeValue;

public interface CategoryAttributeValueService {
    CategoryAttributeValue addValueToAttribute(CategoryAttributeValueRequestDto dto);
    List<CategoryAttributeValue> getValuesByAttribute(String attributeId);
} 
