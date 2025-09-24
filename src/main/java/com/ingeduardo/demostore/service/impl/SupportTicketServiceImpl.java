package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.SupportTicketRequestDto;
import com.ingeduardo.demostore.dto.SupportTicketResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Customer;
import com.ingeduardo.demostore.model.SupportTicket;
import com.ingeduardo.demostore.repository.CustomerRepository;
import com.ingeduardo.demostore.repository.SupportTicketRepository;
import com.ingeduardo.demostore.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<SupportTicketResponseDto> getAllSupportTickets() {
        return supportTicketRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupportTicketResponseDto getSupportTicketById(Long id) {
        SupportTicket supportTicket = supportTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Support Ticket not found with id: " + id));
        return convertToDto(supportTicket);
    }

    @Override
    public SupportTicketResponseDto createSupportTicket(SupportTicketRequestDto requestDto) {
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setSubject(requestDto.getSubject());
        supportTicket.setMessage(requestDto.getMessage());

        Customer customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + requestDto.getCustomerId()));
        supportTicket.setCustomer(customer);

        supportTicket.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : com.ingeduardo.demostore.model.enums.SupportTicketStatus.OPEN);

        SupportTicket savedTicket = supportTicketRepository.save(supportTicket);
        return convertToDto(savedTicket);
    }

    @Override
    public SupportTicketResponseDto updateSupportTicket(Long id, SupportTicketRequestDto requestDto) {
        SupportTicket supportTicket = supportTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Support Ticket not found with id: " + id));

        supportTicket.setSubject(requestDto.getSubject());
        supportTicket.setMessage(requestDto.getMessage());
        supportTicket.setStatus(requestDto.getStatus());

        SupportTicket updatedTicket = supportTicketRepository.save(supportTicket);
        return convertToDto(updatedTicket);
    }

    @Override
    public void deleteSupportTicket(Long id) {
        if (!supportTicketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Support Ticket not found with id: " + id);
        }
        supportTicketRepository.deleteById(id);
    }

    @Override
    public List<SupportTicketResponseDto> getSupportTicketsByCustomerId(String customerId) {
        return supportTicketRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SupportTicketResponseDto convertToDto(SupportTicket supportTicket) {
        SupportTicketResponseDto dto = new SupportTicketResponseDto();
        dto.setId(supportTicket.getId());
        dto.setSubject(supportTicket.getSubject());
        dto.setMessage(supportTicket.getMessage());
        dto.setCustomerId(supportTicket.getCustomer().getId());
        dto.setCustomerName(supportTicket.getCustomer().getName());
        dto.setStatus(supportTicket.getStatus());
        dto.setCreatedAt(supportTicket.getCreatedAt());
        dto.setUpdatedAt(supportTicket.getUpdatedAt());
        return dto;
    }
}
