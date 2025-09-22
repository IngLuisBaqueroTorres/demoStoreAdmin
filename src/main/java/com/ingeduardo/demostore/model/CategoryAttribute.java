package com.ingeduardo.demostore.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "category_attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAttribute {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column
    private String type; 

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @PrePersist
    public void ensureId() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }
}
