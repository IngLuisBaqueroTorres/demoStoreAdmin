package com.ingeduardo.demostore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "settings")
@Data
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;
    private String contactEmail;
    private String logoUrl;
    private BigDecimal taxRate;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(columnDefinition = "TEXT")
    private String privacyPolicy;

    private String timezone;
    private String currency;
    private String language;
}
