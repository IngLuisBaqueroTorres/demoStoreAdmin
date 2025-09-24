package com.ingeduardo.demostore.dto;

import lombok.Data;
import java.util.Set;

@Data
public class RoleResponseDto {
    private Long id;
    private String name;
    private Set<PermissionResponseDto> permissions;
}
