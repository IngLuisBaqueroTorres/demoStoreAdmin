package com.ingeduardo.demostore.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingeduardo.demostore.model.Product;
import com.ingeduardo.demostore.model.ProductAttribute;
import com.ingeduardo.demostore.model.ProductAttributeValue;
import com.ingeduardo.demostore.repository.ProductAttributeValueRepository;
import com.ingeduardo.demostore.repository.ProductAttributeRepository;
import com.ingeduardo.demostore.repository.ProductRepository;

@Service
public class ProductAttributeValueService {

    @Autowired
    private ProductAttributeValueRepository attributeValueRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAttributeRepository attributeRepository;

    public List<ProductAttributeValue> getAttributesByProduct(String productId) {
        return attributeValueRepository.findByProductId(productId);
    }

    public ProductAttributeValue addOrUpdateAttributeValue(String productId, UUID attributeId, String value) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        ProductAttribute attribute = attributeRepository.findById(attributeId.toString())
            .orElseThrow(() -> new RuntimeException("Attribute not found: " + attributeId));

        // Buscar si ya existe la combinación producto + atributo
        List<ProductAttributeValue> existingValues = attributeValueRepository.findByProductId(productId);
        for (ProductAttributeValue pav : existingValues) {
            if (pav.getAttribute().getId().equals(attributeId.toString())) {
                pav.setValue(value);
                return attributeValueRepository.save(pav);
            }
        }

        // Si no existe, crear nuevo
        ProductAttributeValue pav = new ProductAttributeValue(product, attribute, value);
        return attributeValueRepository.save(pav);
    }

    public void deleteAttributeValue(String id) {
        attributeValueRepository.deleteById(id);
    }
}
