// CategoryAttributeValueResponseDto.java
package com.ingeduardo.demostore.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryAttributeValueResponseDto {
    private String id;
    private String value;
    private String attributeId;
}
