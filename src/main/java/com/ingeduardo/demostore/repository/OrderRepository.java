package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Customer;
import com.ingeduardo.demostore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomer(Customer customer);
}
