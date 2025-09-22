package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Category> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    Page<Category> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    @Query("SELECT c FROM Category c " +
           "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Category> search(@Param("name") String name,
                          @Param("description") String description,
                          Pageable pageable);
}
