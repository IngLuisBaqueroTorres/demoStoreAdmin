package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.PermissionResponseDto;
import com.ingeduardo.demostore.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PermissionResponseDto> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')") // Only SUPER_ADMIN can create permissions
    public ResponseEntity<PermissionResponseDto> createPermission(@RequestBody PermissionRequestDto requestDto) {
        PermissionResponseDto createdPermission = permissionService.createPermission(requestDto.getName(), requestDto.getDescription());
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')") // Only SUPER_ADMIN can update permissions
    public ResponseEntity<PermissionResponseDto> updatePermission(@PathVariable Long id, @RequestBody PermissionRequestDto requestDto) {
        return ResponseEntity.ok(permissionService.updatePermission(id, requestDto.getName(), requestDto.getDescription()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')") // Only SUPER_ADMIN can delete permissions
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
