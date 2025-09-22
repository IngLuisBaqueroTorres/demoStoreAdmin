package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.ProductAttributeValueRequestDto;
import com.ingeduardo.demostore.dto.ProductAttributeValueResponseDto;

import java.util.List;

public interface ProductAttributeValueService {
   ProductAttributeValueResponseDto create(ProductAttributeValueRequestDto request);
    ProductAttributeValueResponseDto update(String id, ProductAttributeValueRequestDto request);
    void delete(String id);
    List<ProductAttributeValueResponseDto> findByProductId(String productId);
}
