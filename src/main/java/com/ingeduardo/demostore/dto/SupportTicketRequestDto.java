package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.SupportTicketStatus;
import lombok.Data;

@Data
public class SupportTicketRequestDto {
    private String subject;
    private String message;
    private String customerId;
    private SupportTicketStatus status;
}
