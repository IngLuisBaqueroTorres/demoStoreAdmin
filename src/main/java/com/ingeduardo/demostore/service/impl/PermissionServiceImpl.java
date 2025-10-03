package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.PermissionResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Permission;
import com.ingeduardo.demostore.repository.PermissionRepository;
import com.ingeduardo.demostore.repository.RoleRepository;
import com.ingeduardo.demostore.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<PermissionResponseDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionResponseDto getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
        return convertToDto(permission);
    }

    @Override
    public PermissionResponseDto createPermission(String name, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        Permission savedPermission = permissionRepository.save(permission);
        return convertToDto(savedPermission);
    }

    @Override
    public PermissionResponseDto updatePermission(Long id, String name, String description) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
        permission.setName(name);
        permission.setDescription(description);
        Permission updatedPermission = permissionRepository.save(permission);
        return convertToDto(updatedPermission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        Permission permissionToDelete = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));

        // Find all roles that have this permission
        roleRepository.findAll().forEach(role -> {
            if (role.getPermissions().contains(permissionToDelete)) {
                role.getPermissions().remove(permissionToDelete);
                roleRepository.save(role);
            }
        });

        // Now it's safe to delete the permission
        permissionRepository.deleteById(id);
    }

    private PermissionResponseDto convertToDto(Permission permission) {
        PermissionResponseDto dto = new PermissionResponseDto();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        return dto;
    }
}
