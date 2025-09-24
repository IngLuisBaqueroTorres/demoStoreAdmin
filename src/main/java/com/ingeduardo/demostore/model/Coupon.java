package com.ingeduardo.demostore.model;

import com.ingeduardo.demostore.model.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    private LocalDate expiryDate;

    private Integer usageLimit;

    private Integer timesUsed = 0;

    private BigDecimal minPurchaseAmount;

    private Boolean isActive = true;
}
