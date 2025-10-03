package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c " +
           "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
           "AND (:phone IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%')))")
    Page<Customer> search(@Param("name") String name,
                          @Param("email") String email,
                          @Param("phone") String phone,
                          Pageable pageable);

    /**
     * Busca clientes cuyo nombre, email o teléfono coincidan parcialmente con el query.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Customer> searchByQuery(@Param("query") String query, Pageable pageable);
}