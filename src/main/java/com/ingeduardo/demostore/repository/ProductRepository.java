package com.ingeduardo.demostore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  
    @Query("SELECT p FROM Product p " +
           "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:category IS NULL OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%'))) " +
           "AND (:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%')))")
    Page<Product> search(@Param("name") String name,
                         @Param("category") String category,
                         @Param("brand") String brand,
                         Pageable pageable);
}
