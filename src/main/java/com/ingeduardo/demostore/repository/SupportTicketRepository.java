package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByCustomerId(String customerId);
}
