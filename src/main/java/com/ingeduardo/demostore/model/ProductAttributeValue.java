package com.ingeduardo.demostore.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class ProductAttributeValue {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_attribute_id")
    private ProductAttribute attribute;

    @Column(nullable = false, length = 255)
    private String value;

    public ProductAttributeValue() {
        this.id = UUID.randomUUID().toString();
    }

    public ProductAttributeValue(Product product, ProductAttribute attribute, String value) {
        this.id = UUID.randomUUID().toString();
        this.product = product;
        this.attribute = attribute;
        this.value = value;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(ProductAttribute attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
