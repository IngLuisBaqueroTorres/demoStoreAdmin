package com.ingeduardo.demostore.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private String customerId;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemRequestDto> items;
    private String couponCode;
    private Long shippingMethodId;
}
