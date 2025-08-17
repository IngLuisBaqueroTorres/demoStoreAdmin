package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, String> {

    List<ProductAttributeValue> findByProductId(String productId);

    Optional<ProductAttributeValue> findByProductIdAndAttributeId(String productId, String attributeId);
}
