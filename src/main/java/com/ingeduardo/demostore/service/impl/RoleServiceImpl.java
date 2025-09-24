package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.PermissionResponseDto;
import com.ingeduardo.demostore.dto.RoleRequestDto;
import com.ingeduardo.demostore.dto.RoleResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Permission;
import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.repository.PermissionRepository;
import com.ingeduardo.demostore.repository.RoleRepository;
import com.ingeduardo.demostore.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return convertToDto(role);
    }

    @Override
    @Transactional
    public RoleResponseDto createRole(RoleRequestDto requestDto) {
        Role role = new Role();
        role.setName(requestDto.getName());
        if (requestDto.getPermissionIds() != null && !requestDto.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(requestDto.getPermissionIds()));
            role.setPermissions(permissions);
        }
        Role savedRole = roleRepository.save(role);
        return convertToDto(savedRole);
    }

    @Override
    @Transactional
    public RoleResponseDto updateRole(Long id, RoleRequestDto requestDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        role.setName(requestDto.getName());
        if (requestDto.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(requestDto.getPermissionIds()));
            role.setPermissions(permissions);
        }
        Role updatedRole = roleRepository.save(role);
        return convertToDto(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleResponseDto assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        Set<Permission> permissionsToAdd = new HashSet<>(permissionRepository.findAllById(permissionIds));
        role.getPermissions().addAll(permissionsToAdd);
        Role updatedRole = roleRepository.save(role);
        return convertToDto(updatedRole);
    }

    @Override
    @Transactional
    public RoleResponseDto removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        Set<Permission> permissionsToRemove = new HashSet<>(permissionRepository.findAllById(permissionIds));
        role.getPermissions().removeAll(permissionsToRemove);
        Role updatedRole = roleRepository.save(role);
        return convertToDto(updatedRole);
    }

    private RoleResponseDto convertToDto(Role role) {
        RoleResponseDto dto = new RoleResponseDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setPermissions(role.getPermissions().stream()
                .map(this::convertToPermissionDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    private PermissionResponseDto convertToPermissionDto(Permission permission) {
        PermissionResponseDto dto = new PermissionResponseDto();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        return dto;
    }
}
