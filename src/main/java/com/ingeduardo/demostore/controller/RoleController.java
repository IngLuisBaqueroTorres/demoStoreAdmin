package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.RoleRequestDto;
import com.ingeduardo.demostore.dto.RoleResponseDto;
import com.ingeduardo.demostore.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody RoleRequestDto requestDto) {
        RoleResponseDto createdRole = roleService.createRole(requestDto);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable Long id, @RequestBody RoleRequestDto requestDto) {
        return ResponseEntity.ok(roleService.updateRole(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<RoleResponseDto> assignPermissionsToRole(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(id, permissionIds));
    }

    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<RoleResponseDto> removePermissionsFromRole(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        return ResponseEntity.ok(roleService.removePermissionsFromRole(id, permissionIds));
    }
}
