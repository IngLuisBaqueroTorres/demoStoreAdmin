package com.ingeduardo.demostore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingeduardo.demostore.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByActiveTrue();
}
