package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Customer;
import com.ingeduardo.demostore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomer(Customer customer);

    @Query("SELECT COALESCE(SUM(o.totalAmount - o.discountAmount), 0) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate")
    BigDecimal findTotalSalesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Order> findTop5ByOrderByOrderDateDesc();
}
