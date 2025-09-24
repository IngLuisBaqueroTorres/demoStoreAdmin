package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.SupportTicketRequestDto;
import com.ingeduardo.demostore.dto.SupportTicketResponseDto;

import java.util.List;

public interface SupportTicketService {
    List<SupportTicketResponseDto> getAllSupportTickets();
    SupportTicketResponseDto getSupportTicketById(Long id);
    SupportTicketResponseDto createSupportTicket(SupportTicketRequestDto requestDto);
    SupportTicketResponseDto updateSupportTicket(Long id, SupportTicketRequestDto requestDto);
    void deleteSupportTicket(Long id);
    List<SupportTicketResponseDto> getSupportTicketsByCustomerId(String customerId);
}
