package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.OrderItemResponseDto;
import com.ingeduardo.demostore.dto.OrderResponseDto;
import com.ingeduardo.demostore.dto.OrderUpdateRequest;
import com.ingeduardo.demostore.model.*;
import com.ingeduardo.demostore.repository.OrderRepository;
import com.ingeduardo.demostore.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository; // Asumimos que existe

    public Page<Order> search(String query, Pageable pageable) {
        return orderRepository.searchByQuery(query, pageable);
    }

    public Order findById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    @Transactional
    public Order update(String id, OrderUpdateRequest updateRequest) {
        // 1. Buscar la orden existente
        Order order = findById(id);

        // 2. Actualizar campos simples de la orden
        order.setStatus(updateRequest.getStatus());
        order.setShippingAddress(updateRequest.getShippingAddress());

        // 3. Limpiar los items anteriores para reemplazarlos
        // Esta es una estrategia simple. Otra más compleja podría ser
        // comparar y actualizar, añadir o quitar items individualmente.
        order.getOrderItems().clear();

        // 4. Crear y añadir los nuevos items
        List<OrderItem> newItems = updateRequest.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + itemDto.getProductId()));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPriceAtPurchase(product.getPrice()); // Usar el precio actual del producto
                    return orderItem;
                })
                .toList();

        order.getOrderItems().addAll(newItems);

        // 5. Recalcular el total de la orden
        BigDecimal totalAmount = newItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        // 6. Guardar la orden actualizada
        return orderRepository.save(order);
    }

    public OrderResponseDto mapToOrderResponseDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setShippingCost(order.getShippingCost());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setFinalAmount(order.getFinalAmount());

        Optional.ofNullable(order.getCustomer()).ifPresent(c -> {
            dto.setCustomerId(c.getId());
            dto.setCustomerName(c.getName());
        });

        Optional.ofNullable(order.getCoupon()).ifPresent(c -> dto.setCouponCode(c.getCode()));
        Optional.ofNullable(order.getShippingMethod()).ifPresent(sm -> dto.setShippingMethodName(sm.getName()));

        dto.setItems(order.getOrderItems().stream().map(this::mapToOrderItemResponseDto).toList());

        return dto;
    }

    private OrderItemResponseDto mapToOrderItemResponseDto(OrderItem item) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtPurchase(item.getPriceAtPurchase());
        return dto;
    }
}