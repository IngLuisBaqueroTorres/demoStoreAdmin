package com.ingeduardo.demostore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.model.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName  name);
}

