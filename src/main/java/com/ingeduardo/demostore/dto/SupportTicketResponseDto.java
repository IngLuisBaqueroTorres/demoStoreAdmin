package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.SupportTicketStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupportTicketResponseDto {
    private Long id;
    private String subject;
    private String message;
    private String customerId;
    private String customerName;
    private SupportTicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
