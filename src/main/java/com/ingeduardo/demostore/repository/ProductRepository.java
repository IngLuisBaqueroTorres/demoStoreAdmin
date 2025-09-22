package com.ingeduardo.demostore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ingeduardo.demostore.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
        List<Product> findByIsActiveTrue();

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
                        "AND (:brandName IS NULL OR LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :brandName, '%')))")
        Page<Product> search(@Param("name") String name,
                        @Param("category") String category,
                        @Param("brandName") String brandName,
                        Pageable pageable);

        @Query("SELECT p FROM Product p LEFT JOIN FETCH p.attributes WHERE p.id = :id")
        Optional<Product> findByIdWithAttributes(@Param("id") String id);
}
