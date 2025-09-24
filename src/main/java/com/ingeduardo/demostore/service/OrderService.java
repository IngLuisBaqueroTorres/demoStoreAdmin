package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.OrderItemRequestDto;
import com.ingeduardo.demostore.dto.OrderItemResponseDto;
import com.ingeduardo.demostore.dto.OrderRequestDto;
import com.ingeduardo.demostore.dto.OrderResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.*;
import com.ingeduardo.demostore.model.enums.DiscountType;
import com.ingeduardo.demostore.model.enums.OrderStatus;
import com.ingeduardo.demostore.repository.CouponRepository;
import com.ingeduardo.demostore.repository.CustomerRepository;
import com.ingeduardo.demostore.repository.OrderItemRepository;
import com.ingeduardo.demostore.repository.OrderRepository;
import com.ingeduardo.demostore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + orderRequest.getCustomerId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setBillingAddress(orderRequest.getBillingAddress());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : orderRequest.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(product.getFinalPrice()); // Use final price with discount

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

            // Reduce stock
            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product); // Save updated product stock
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Handle coupon
        if (StringUtils.hasText(orderRequest.getCouponCode())) {
            applyCoupon(orderRequest.getCouponCode(), order);
        }

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return mapToOrderResponseDto(savedOrder);
    }

    private void applyCoupon(String couponCode, Order order) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Invalid coupon code: " + couponCode));

        if (!coupon.getIsActive()) {
            throw new RuntimeException("Coupon is not active.");
        }

        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Coupon has expired.");
        }

        if (coupon.getUsageLimit() != null && coupon.getTimesUsed() >= coupon.getUsageLimit()) {
            throw new RuntimeException("Coupon has reached its usage limit.");
        }

        if (coupon.getMinPurchaseAmount() != null && order.getTotalAmount().compareTo(coupon.getMinPurchaseAmount()) < 0) {
            throw new RuntimeException("Order amount does not meet the minimum purchase amount for this coupon.");
        }

        BigDecimal discountAmount = calculateDiscount(order.getTotalAmount(), coupon);
        order.setDiscountAmount(discountAmount);
        order.setCoupon(coupon);

        coupon.setTimesUsed(coupon.getTimesUsed() + 1);
        couponRepository.save(coupon);
    }

    private BigDecimal calculateDiscount(BigDecimal totalAmount, Coupon coupon) {
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            BigDecimal discount = totalAmount.multiply(coupon.getDiscountValue().divide(new BigDecimal("100")));
            return discount.setScale(2, java.math.RoundingMode.HALF_UP);
        } else if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            return coupon.getDiscountValue();
        }
        return BigDecimal.ZERO;
    }

    public OrderResponseDto getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return mapToOrderResponseDto(order);
    }

    public List<OrderResponseDto> getOrdersByCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        List<Order> orders = orderRepository.findByCustomer(customer);
        return orders.stream().map(this::mapToOrderResponseDto).collect(Collectors.toList());
    }

    // Admin method to get all orders
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponseDto).collect(Collectors.toList());
    }

    // Admin method to update order status
    @Transactional
    public OrderResponseDto updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponseDto(updatedOrder);
    }

    public OrderResponseDto mapToOrderResponseDto(Order order) {
        List<OrderItemResponseDto> itemDtos = order.getOrderItems().stream()
                .map(item -> {
                    OrderItemResponseDto itemDto = new OrderItemResponseDto();
                    itemDto.setProductId(item.getProduct().getId());
                    itemDto.setProductName(item.getProduct().getName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPriceAtPurchase(item.getPriceAtPurchase());
                    return itemDto;
                })
                .collect(Collectors.toList());

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setItems(itemDtos);

        if (order.getCoupon() != null) {
            dto.setCouponCode(order.getCoupon().getCode());
            dto.setDiscountAmount(order.getDiscountAmount());
        } else {
            dto.setDiscountAmount(BigDecimal.ZERO);
        }
        dto.setFinalAmount(order.getFinalAmount());

        return dto;
    }
}
