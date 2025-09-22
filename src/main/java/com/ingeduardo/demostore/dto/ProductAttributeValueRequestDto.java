package com.ingeduardo.demostore.dto;

import java.util.UUID;

import com.ingeduardo.demostore.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValueRequestDto {
    private Product product;
    private String attributeId;
    private String value;

   
}
