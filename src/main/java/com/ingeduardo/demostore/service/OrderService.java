package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.*;
import com.ingeduardo.demostore.dto.OrderResponseDto;
import com.ingeduardo.demostore.model.*;
import com.ingeduardo.demostore.model.enums.DiscountType;
import com.ingeduardo.demostore.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShippingMethodRepository shippingMethodRepository; // Asumimos que existe
    private final ProductRepository productRepository; // Asumimos que existe
    private final CustomerRepository customerRepository; // Añadir para buscar clientes
    private final CouponRepository couponRepository; // Añadir para buscar cupones

    public Page<Order> search(String query, Pageable pageable) {
        return orderRepository.searchByQuery(query, pageable);
    }

    public Order findById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    @Transactional
    public Order update(String id, OrderUpdateRequestDto updateRequest) {
        // 1. Buscar la orden existente
        Order order = findById(id);

        // 2. Actualizar campos opcionales
        if (updateRequest.getStatus() != null) {
            order.setStatus(updateRequest.getStatus());
        }
        if (updateRequest.getTrackingNumber() != null) {
            order.setTrackingNumber(updateRequest.getTrackingNumber());
        }
        // Solo actualiza si se proporciona una nueva dirección
        if (updateRequest.getShippingAddress() != null && !updateRequest.getShippingAddress().isEmpty()) {
            order.setShippingAddress(updateRequest.getShippingAddress());
        }
        if (updateRequest.getBillingAddress() != null && !updateRequest.getBillingAddress().isEmpty()) {
            order.setBillingAddress(updateRequest.getBillingAddress());
        }
        if (updateRequest.getCustomerId() != null) {
            Customer customer = customerRepository.findById(updateRequest.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + updateRequest.getCustomerId()));
            order.setCustomer(customer);
        }
        // La lógica para shippingMethodId y couponCode es un poco más compleja
        // porque un valor nulo o vacío puede significar "quitar el método/cupón".
        if (updateRequest.getShippingMethodId() != null) {
            ShippingMethod shippingMethod = shippingMethodRepository.findById(updateRequest.getShippingMethodId())
                    .orElseThrow(() -> new EntityNotFoundException("ShippingMethod not found with ID: " + updateRequest.getShippingMethodId()));
            order.setShippingMethod(shippingMethod);
        } else if (updateRequest.getShippingMethodId() == null && updateRequest.getCouponCode() == null && updateRequest.getItems() == null) {
            // Si no se envía nada que afecte los totales, no hacemos nada con el método de envío
        } else {
            order.setShippingMethod(null); // Permite quitar el método de envío
        }

        // Lógica completa para añadir, cambiar o quitar un cupón.
        if (updateRequest.getCouponCode() != null) {
            if (updateRequest.getCouponCode().isEmpty()) {
                order.setCoupon(null); // Quitar el cupón si se envía un string vacío
            } else {
                Coupon coupon = couponRepository.findByCode(updateRequest.getCouponCode()).orElseThrow(() -> new EntityNotFoundException("Coupon not found with code: " + updateRequest.getCouponCode()));
                order.setCoupon(coupon); // Asignar el nuevo cupón
            }
        }

        if (updateRequest.getItems() != null && !updateRequest.getItems().isEmpty()) {
            // 3. Limpiar los items anteriores para reemplazarlos
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
                    .collect(Collectors.toList());

            order.getOrderItems().addAll(newItems);

            // Recalcular el subtotal de los items
            BigDecimal totalAmount = newItems.stream()
                    .map(item -> item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(totalAmount);
        }

        // 5. Recalcular descuentos y total final
        recalculateTotals(order);

        // 6. Guardar la orden actualizada y devolverla
        return orderRepository.save(order);
    }

    private void recalculateTotals(Order order) {
        // Recalcular el costo de envío basado en el método de envío actual de la orden
        BigDecimal shippingCost = BigDecimal.ZERO;
        if (order.getShippingMethod() != null) {
            shippingCost = Optional.ofNullable(order.getShippingMethod().getCost()).orElse(BigDecimal.ZERO);
        }
        order.setShippingCost(shippingCost);

        // Recalcular el descuento basado en el cupón actual de la orden y su tipo
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (order.getCoupon() != null) {
            // Aquí iría la lógica para calcular el descuento basado en el cupón.
            // Por simplicidad, asumimos un valor fijo o un porcentaje del totalAmount.
            // Esta lógica debería estar en un CouponService idealmente.
            // Asegurémonos de que el cupón esté activo y sea válido.
            if (order.getCoupon().getIsActive()) {
                Coupon coupon = order.getCoupon();
                if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
                    BigDecimal total = Optional.ofNullable(order.getTotalAmount()).orElse(BigDecimal.ZERO);
                    BigDecimal discountValue = Optional.ofNullable(coupon.getDiscountValue()).orElse(BigDecimal.ZERO);
                    discountAmount = total.multiply(discountValue.divide(new BigDecimal("100")));
                } else { // FIXED_AMOUNT
                    discountAmount = Optional.ofNullable(coupon.getDiscountValue()).orElse(BigDecimal.ZERO);
                }
            }
        }
        order.setDiscountAmount(discountAmount);

        BigDecimal totalAmount = Optional.ofNullable(order.getTotalAmount()).orElse(BigDecimal.ZERO);
        order.setFinalAmount(totalAmount.add(shippingCost).subtract(discountAmount));
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