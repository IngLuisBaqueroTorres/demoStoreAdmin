package com.ingeduardo.demostore.config;

import com.ingeduardo.demostore.model.Permission;
import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.model.enums.RoleName;
import com.ingeduardo.demostore.repository.PermissionRepository;
import com.ingeduardo.demostore.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ingeduardo.demostore.model.enums.RoleName.*;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Define permissions
        Permission viewOrders = createPermissionIfNotFound("VIEW_ORDERS", "View all orders");
        Permission manageOrders = createPermissionIfNotFound("MANAGE_ORDERS", "Create, update, delete orders");
        Permission viewProducts = createPermissionIfNotFound("VIEW_PRODUCTS", "View all products");
        Permission manageProducts = createPermissionIfNotFound("MANAGE_PRODUCTS", "Create, update, delete products");
        Permission viewUsers = createPermissionIfNotFound("VIEW_USERS", "View all users");
        Permission manageUsers = createPermissionIfNotFound("MANAGE_USERS", "Create, update, delete users");
        Permission viewSettings = createPermissionIfNotFound("VIEW_SETTINGS", "View general settings");
        Permission manageSettings = createPermissionIfNotFound("MANAGE_SETTINGS", "Update general settings");
        Permission viewRoles = createPermissionIfNotFound("VIEW_ROLES", "View all roles");
        Permission manageRoles = createPermissionIfNotFound("MANAGE_ROLES", "Create, update, delete roles");
        Permission viewPermissions = createPermissionIfNotFound("VIEW_PERMISSIONS", "View all permissions");
        Permission managePermissions = createPermissionIfNotFound("MANAGE_PERMISSIONS", "Create, update, delete permissions");
        Permission viewShippingMethods = createPermissionIfNotFound("VIEW_SHIPPING_METHODS", "View all shipping methods");
        Permission manageShippingMethods = createPermissionIfNotFound("MANAGE_SHIPPING_METHODS", "Create, update, delete shipping methods");
        Permission viewPayments = createPermissionIfNotFound("VIEW_PAYMENTS", "View all payments");
        Permission managePayments = createPermissionIfNotFound("MANAGE_PAYMENTS", "Manage payment statuses");

        // Get all permissions for SUPER_ADMIN
        List<Permission> allPermissions = permissionRepository.findAll();

        // Create roles and assign permissions
        createRoleIfNotFound(SUPER_ADMIN, new HashSet<>(allPermissions));
        createRoleIfNotFound(ADMIN, new HashSet<>(Arrays.asList(
                viewOrders, manageOrders,
                viewProducts, manageProducts,
                viewUsers, viewSettings, manageSettings,
                viewShippingMethods, manageShippingMethods,
                viewPayments, managePayments
        )));
        createRoleIfNotFound(USER, new HashSet<>(Arrays.asList(
                viewOrders, viewProducts
        )));

        System.out.println("🔧 Roles and Permissions initialized.");
    }

    private Permission createPermissionIfNotFound(String name, String description) {
        return permissionRepository.findByName(name).orElseGet(() -> {
            Permission permission = new Permission();
            permission.setName(name);
            permission.setDescription(description);
            return permissionRepository.save(permission);
        });
    }

    private void createRoleIfNotFound(RoleName roleName, Set<Permission> permissions) {
        roleRepository.findByName(roleName.name()).ifPresentOrElse(existingRole -> {
            // Update existing role's permissions if they are different
            if (!existingRole.getPermissions().equals(permissions)) {
                existingRole.setPermissions(permissions);
                roleRepository.save(existingRole);
                System.out.println("🔧 Updated permissions for role: " + roleName);
            } else {
                System.out.println("✅ Role already exists: " + roleName);
            }
        }, () -> {
            Role newRole = new Role();
            newRole.setName(roleName.name());
            newRole.setPermissions(permissions);
            roleRepository.save(newRole);
            System.out.println("🔧 Created role: " + roleName);
        });
    }

}

