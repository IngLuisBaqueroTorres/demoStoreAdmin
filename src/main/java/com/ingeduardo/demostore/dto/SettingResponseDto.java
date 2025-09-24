package com.ingeduardo.demostore.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettingResponseDto {
    private Long id;
    private String storeName;
    private String contactEmail;
    private String logoUrl;
    private BigDecimal taxRate;
    private String termsAndConditions;
    private String privacyPolicy;
    private String timezone;
    private String currency;
    private String language;
}
