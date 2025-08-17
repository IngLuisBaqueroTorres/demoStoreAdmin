package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.model.ProductAttribute;
import com.ingeduardo.demostore.repository.ProductAttributeRepository;
import com.ingeduardo.demostore.service.ProductAttributeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository repository;

    public ProductAttributeServiceImpl(ProductAttributeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductAttribute create(ProductAttribute attribute) {
        return repository.save(attribute);
    }

    @Override
    public List<ProductAttribute> getAll() {
        return repository.findAll();
    }

    @Override
    public ProductAttribute getById(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Attribute not found"));
    }

    @Override
    public ProductAttribute update(String id, ProductAttribute attribute) {
        ProductAttribute existing = getById(id);
        existing.setName(attribute.getName());
        existing.setType(attribute.getType());
        existing.setDescription(attribute.getDescription());
        return repository.save(existing);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
