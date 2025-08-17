// CategoryAttributeValueServiceImpl.java
package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.CategoryAttributeValueRequestDto;
import com.ingeduardo.demostore.model.*;
import com.ingeduardo.demostore.repository.*;
import com.ingeduardo.demostore.service.CategoryAttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryAttributeValueServiceImpl implements CategoryAttributeValueService {

    private final CategoryAttributeRepository attributeRepository;
    private final CategoryAttributeValueRepository valueRepository;

    @Override
    public CategoryAttributeValue addValueToAttribute(CategoryAttributeValueRequestDto dto) {
        var attribute = attributeRepository.findById(dto.getAttributeId())
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        var value = CategoryAttributeValue.builder()
                .value(dto.getValue())
                .attribute(attribute)
                .build();
        // id se genera en @PrePersist
        return valueRepository.save(value);
    }

    @Override
    public List<CategoryAttributeValue> getValuesByAttribute(String attributeId) {
        return valueRepository.findByAttributeId(attributeId);
    }
}
