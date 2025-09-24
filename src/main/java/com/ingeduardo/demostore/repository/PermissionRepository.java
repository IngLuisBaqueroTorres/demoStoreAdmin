package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
