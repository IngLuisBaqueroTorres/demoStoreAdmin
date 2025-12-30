package com.ingeduardo.demostore.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private BigDecimal discount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer stock;
    private Boolean isActive = true;
    private Boolean isSoldOut = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductAttributeValue> attributes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    public void setSoldOut(Boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    public BigDecimal getFinalPrice() {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        if (discount == null || discount.compareTo(BigDecimal.ZERO) <= 0 || discount.compareTo(new BigDecimal("100")) >= 0) {
            return price;
        }
        BigDecimal discountMultiplier = discount.divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal discountAmount = price.multiply(discountMultiplier);
        return price.subtract(discountAmount);
    }
}
