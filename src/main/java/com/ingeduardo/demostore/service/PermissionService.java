package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.PermissionResponseDto;

import java.util.List;

public interface PermissionService {
    List<PermissionResponseDto> getAllPermissions();
    PermissionResponseDto getPermissionById(Long id);
    PermissionResponseDto createPermission(String name, String description);
    PermissionResponseDto updatePermission(Long id, String name, String description);
    void deletePermission(Long id);
}
