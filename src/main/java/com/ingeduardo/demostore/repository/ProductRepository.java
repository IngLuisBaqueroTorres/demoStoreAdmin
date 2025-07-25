package com.ingeduardo.demostore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ingeduardo.demostore.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Product> findByNameContainingIgnoreCase(String name);

     @Query("SELECT p FROM Product p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Product> findByDescriptionContainingIgnoreCase(String description);

    List<Product> findByCategory_NameIgnoreCase(String category);

    List<Product> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String name, String description);

    List<Product> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCaseAndCategory_NameIgnoreCase(
            String name, String description, String category);

}
