package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private String id;
    private String customerId;
    private String customerName;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemResponseDto> items;
}
