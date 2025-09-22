package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.model.ProductAttribute;
import com.ingeduardo.demostore.model.ProductAttributeValue;
import com.ingeduardo.demostore.repository.ProductAttributeRepository;
import com.ingeduardo.demostore.repository.ProductAttributeValueRepository;
import com.ingeduardo.demostore.dto.ProductAttributeValueResponseDto;

import com.ingeduardo.demostore.dto.ProductAttributeValueRequestDto;
import com.ingeduardo.demostore.repository.ProductRepository;
import com.ingeduardo.demostore.service.ProductAttributeValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductAttributeValueServiceImpl implements ProductAttributeValueService {

   private final ProductAttributeValueRepository repository;

   public ProductAttributeValueServiceImpl(ProductAttributeValueRepository repository) {
       this.repository = repository;
   }

   @Override
   public ProductAttributeValueResponseDto create(ProductAttributeValueRequestDto request) {

        ProductAttributeValue pav = new ProductAttributeValue();

        pav.setProduct(request.getProduct());
        pav.setAttributeId(request.getAttributeId());
        pav.setValue(request.getValue());

        ProductAttributeValue savedPav = repository.save(pav);
        return mapToResponse(savedPav);
   }

    @Override
    public ProductAttributeValueResponseDto update(String id, ProductAttributeValueRequestDto request) {
        ProductAttributeValue pav = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductAttributeValue not found"));

        pav.setProduct(request.getProduct());
        pav.setAttributeId(request.getAttributeId());
        pav.setValue(request.getValue());

        ProductAttributeValue updatedPav = repository.save(pav);
        return mapToResponse(updatedPav);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<ProductAttributeValueResponseDto> findByProductId(String productId) {
        return repository.findByProductId(productId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductAttributeValueResponseDto mapToResponse(ProductAttributeValue pav) {
        ProductAttributeValueResponseDto dto = new ProductAttributeValueResponseDto();
        dto.setId(pav.getId());
        dto.setProductId(pav.getProduct().getId());
        dto.setAttributeId(pav.getAttributeId());
        dto.setValue(pav.getValue());
        return dto;
    }
}
