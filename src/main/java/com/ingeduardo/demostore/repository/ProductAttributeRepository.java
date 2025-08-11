package com.ingeduardo.demostore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingeduardo.demostore.model.ProductAttribute;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, String> {
    // Puedes agregar métodos personalizados si quieres
}
