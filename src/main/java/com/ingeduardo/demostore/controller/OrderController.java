package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.OrderRequestDto;
import com.ingeduardo.demostore.dto.OrderResponseDto;
import com.ingeduardo.demostore.model.enums.OrderStatus;
import com.ingeduardo.demostore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_ORDERS')")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequest) {
        OrderResponseDto createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_ORDERS')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String id) {
        OrderResponseDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/by-customer/{customerId}")
    @PreAuthorize("hasPermission(null, 'VIEW_ORDERS')")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByCustomer(@PathVariable String customerId) {
        List<OrderResponseDto> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_ORDERS')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasPermission(null, 'MANAGE_ORDERS')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable String id, @RequestParam OrderStatus status) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{id}/tracking")
    @PreAuthorize("hasPermission(null, 'MANAGE_ORDERS')")
    public ResponseEntity<OrderResponseDto> updateTrackingNumber(@PathVariable String id, @RequestBody com.ingeduardo.demostore.dto.UpdateTrackingNumberRequestDto request) {
        OrderResponseDto updatedOrder = orderService.updateTrackingNumber(id, request.getTrackingNumber());
        return ResponseEntity.ok(updatedOrder);
    }
}
