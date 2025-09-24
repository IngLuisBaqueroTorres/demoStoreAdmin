package com.ingeduardo.demostore.model;

import com.ingeduardo.demostore.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // "Order" might be a reserved keyword in some DBs
@Data
public class Order {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String shippingAddress;

    private String billingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    @PrePersist
    public void prePersist() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    public BigDecimal getFinalAmount() {
        if (totalAmount == null) {
            return BigDecimal.ZERO;
        }
        if (discountAmount == null) {
            return totalAmount;
        }
        return totalAmount.subtract(discountAmount);
    }
}
