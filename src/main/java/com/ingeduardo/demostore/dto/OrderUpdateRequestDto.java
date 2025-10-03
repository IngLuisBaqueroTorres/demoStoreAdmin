package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.Address;
import com.ingeduardo.demostore.model.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequestDto {
    private Address shippingAddress;
    private Address billingAddress;
    private OrderStatus status;
    private String trackingNumber;
    private Long shippingMethodId;
    private List<OrderItemRequestDto> items;
}