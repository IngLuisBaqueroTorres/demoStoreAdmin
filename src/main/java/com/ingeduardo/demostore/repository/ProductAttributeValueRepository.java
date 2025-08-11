package com.ingeduardo.demostore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingeduardo.demostore.model.ProductAttributeValue;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, String> {
    List<ProductAttributeValue> findByProductId(String productId);
}
