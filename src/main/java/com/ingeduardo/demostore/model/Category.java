package com.ingeduardo.demostore.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Category {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    private String name;
    private String description;

    public Category() {
    }

    public Category(String id) {
        this.id = id;
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // --- Auto generar el id si no existe ---
    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    // Getters & Setters
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
}
