package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.RoleRequestDto;
import com.ingeduardo.demostore.dto.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto getRoleById(Long id);
    RoleResponseDto createRole(RoleRequestDto requestDto);
    RoleResponseDto updateRole(Long id, RoleRequestDto requestDto);
    void deleteRole(Long id);
    RoleResponseDto assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    RoleResponseDto removePermissionsFromRole(Long roleId, List<Long> permissionIds);
}
