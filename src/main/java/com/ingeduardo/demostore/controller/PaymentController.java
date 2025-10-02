package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.PaymentResponseDto;
import com.ingeduardo.demostore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_PAYMENTS')")
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_PAYMENTS')")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasPermission(null, 'VIEW_PAYMENTS')")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsForOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsForOrder(orderId));
    }
}
