package com.ingeduardo.demostore.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class CategoryAttribute {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private ProductAttribute attribute;

    // Constructor vacío
    public CategoryAttribute() {}

    // Getters y setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(ProductAttribute attribute) {
        this.attribute = attribute;
    }
}
