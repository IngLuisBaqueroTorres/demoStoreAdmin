package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    boolean existsByName(String name);
    java.util.Optional<Brand> findByNameIgnoreCase(String name);

    @Query("SELECT b FROM Brand b " +
           "WHERE (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:description IS NULL OR LOWER(b.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Brand> search(@Param("name") String name,
                       @Param("description") String description,
                       Pageable pageable);
}
