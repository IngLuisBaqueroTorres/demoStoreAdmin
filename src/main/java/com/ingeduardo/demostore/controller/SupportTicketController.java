package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.SupportTicketRequestDto;
import com.ingeduardo.demostore.dto.SupportTicketResponseDto;
import com.ingeduardo.demostore.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support-tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @GetMapping
    @PreAuthorize("hasPermission('VIEW_SUPPORT_TICKETS')")
    public ResponseEntity<List<SupportTicketResponseDto>> getAllSupportTickets() {
        return ResponseEntity.ok(supportTicketService.getAllSupportTickets());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('VIEW_SUPPORT_TICKETS')")
    public ResponseEntity<SupportTicketResponseDto> getSupportTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(supportTicketService.getSupportTicketById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')") // Any logged-in user can create a ticket
    public ResponseEntity<SupportTicketResponseDto> createSupportTicket(@RequestBody SupportTicketRequestDto requestDto) {
        SupportTicketResponseDto createdTicket = supportTicketService.createSupportTicket(requestDto);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('MANAGE_SUPPORT_TICKETS')")
    public ResponseEntity<SupportTicketResponseDto> updateSupportTicket(@PathVariable Long id, @RequestBody SupportTicketRequestDto requestDto) {
        return ResponseEntity.ok(supportTicketService.updateSupportTicket(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('MANAGE_SUPPORT_TICKETS')")
    public ResponseEntity<Void> deleteSupportTicket(@PathVariable Long id) {
        supportTicketService.deleteSupportTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasPermission('VIEW_SUPPORT_TICKETS') or (authentication.principal.id == #customerId)") // Admins can view all, users can view their own
    public ResponseEntity<List<SupportTicketResponseDto>> getSupportTicketsByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(supportTicketService.getSupportTicketsByCustomerId(customerId));
    }
}
