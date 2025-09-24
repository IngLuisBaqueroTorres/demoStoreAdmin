package com.ingeduardo.demostore.config;

import com.ingeduardo.demostore.model.Permission;
import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.model.User;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        // For now, we'll assume targetDomainObject is not used for object-level permissions
        return hasPrivilege(authentication, permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(authentication, permission.toString());
    }

    private boolean hasPrivilege(Authentication authentication, String permission) {
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            // Check if the user has SUPER_ADMIN role
            boolean isSuperAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("SUPER_ADMIN"));
            if (isSuperAdmin) {
                return true; // SUPER_ADMIN has all permissions
            }

            // Check if the user has the specific permission through their roles
            Set<String> userPermissions = user.getRoles().stream()
                    .map(Role::getPermissions)
                    .flatMap(Collection::stream)
                    .map(Permission::getName)
                    .collect(Collectors.toSet());
            return userPermissions.contains(permission);
        }
        return false;
    }
}
