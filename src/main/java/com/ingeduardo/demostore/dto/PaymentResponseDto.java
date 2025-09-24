package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private String id;
    private String orderId;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus status;
    private String transactionId;
}
