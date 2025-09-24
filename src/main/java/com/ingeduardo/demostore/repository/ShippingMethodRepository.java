package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {
}
