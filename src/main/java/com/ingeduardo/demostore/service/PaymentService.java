package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> getPaymentsForOrder(String orderId);
    List<PaymentResponseDto> getAllPayments();
    PaymentResponseDto getPaymentById(String paymentId);
}
