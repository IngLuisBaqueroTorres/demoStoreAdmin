package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequestDto {
    private String shippingAddress;
    private String billingAddress;
    private OrderStatus status;
    private String trackingNumber;
    private Long shippingMethodId;
    private List<OrderItemRequestDto> items;
    private String customerId;
    private String couponCode;
}