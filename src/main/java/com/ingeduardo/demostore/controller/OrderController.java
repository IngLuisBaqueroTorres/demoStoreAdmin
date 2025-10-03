package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.OrderUpdateRequest;
import com.ingeduardo.demostore.dto.OrderResponseDto;
import com.ingeduardo.demostore.model.Order;
import com.ingeduardo.demostore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasPermission(null, 'VIEW_ORDERS')")
    public ResponseEntity<Page<OrderResponseDto>> searchOrders(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
        Sort sortOrder = Sort.by(direction, sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Order> orders = orderService.search(query, pageable);
        Page<OrderResponseDto> orderDtos = orders.map(orderService::mapToOrderResponseDto);
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_ORDERS')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.mapToOrderResponseDto(orderService.findById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_ORDERS')")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable String id,
            @Valid @RequestBody OrderUpdateRequest updateRequest) {
        Order updatedOrder = orderService.update(id, updateRequest);
        return ResponseEntity.ok(orderService.mapToOrderResponseDto(updatedOrder));
    }
}