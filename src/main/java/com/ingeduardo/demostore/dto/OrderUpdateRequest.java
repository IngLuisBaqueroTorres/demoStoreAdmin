package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequest {

    @NotNull(message = "Status cannot be null")
    private OrderStatus status;

    private String shippingAddress;

    @NotEmpty(message = "Order must have at least one item")
    @Valid // Para que se validen los objetos dentro de la lista
    private List<OrderItemUpdateRequest> items;
}