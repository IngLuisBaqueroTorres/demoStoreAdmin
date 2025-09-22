package com.ingeduardo.demostore.service;

import java.util.List;

import com.ingeduardo.demostore.model.ProductAttribute;

public interface ProductAttributeService {
    ProductAttribute create(ProductAttribute attribute);
    List<ProductAttribute> getAll();
    ProductAttribute getById(String id);
    ProductAttribute update(String id, ProductAttribute attribute);
    void delete(String id);
}
