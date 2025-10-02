package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.PermissionRequestDto;
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
    @PreAuthorize("hasPermission(null, 'VIEW_PERMISSIONS')")
    public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'VIEW_PERMISSIONS')")
    public ResponseEntity<PermissionResponseDto> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PERMISSIONS')")
    public ResponseEntity<PermissionResponseDto> createPermission(@RequestBody PermissionRequestDto requestDto) {
        PermissionResponseDto createdPermission = permissionService.createPermission(requestDto.getName(), requestDto.getDescription());
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PERMISSIONS')")
    public ResponseEntity<PermissionResponseDto> updatePermission(@PathVariable Long id, @RequestBody PermissionRequestDto requestDto) {
        return ResponseEntity.ok(permissionService.updatePermission(id, requestDto.getName(), requestDto.getDescription()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PERMISSIONS')")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
