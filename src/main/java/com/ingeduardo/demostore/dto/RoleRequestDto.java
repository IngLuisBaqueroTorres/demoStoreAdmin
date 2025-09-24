package com.ingeduardo.demostore.dto;

import lombok.Data;
import java.util.Set;

@Data
public class RoleRequestDto {
    private String name;
    private Set<Long> permissionIds;
}
