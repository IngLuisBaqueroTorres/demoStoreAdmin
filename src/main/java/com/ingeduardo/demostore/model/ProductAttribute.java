package com.ingeduardo.demostore.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class ProductAttribute {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String type; 

    public ProductAttribute() {
        
    }

    public ProductAttribute(String name, String description, String type) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
