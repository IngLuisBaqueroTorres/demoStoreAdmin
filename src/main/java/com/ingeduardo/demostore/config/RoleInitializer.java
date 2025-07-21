package com.ingeduardo.demostore.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.ingeduardo.demostore.model.enums.RoleName;
import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        insertRoleIfNotExists("ADMIN");
        insertRoleIfNotExists("USER");
        insertRoleIfNotExists("SUPER_ADMIN"); 
    }

    private void insertRoleIfNotExists(String roleName) {
        RoleName enumRoleName = RoleName.valueOf(roleName);

        if (roleRepository.findByName(enumRoleName).isEmpty()) {
            Role role = new Role();
            role.setName(enumRoleName);
            roleRepository.save(role);
            System.out.println("🔧 Rol created: " + roleName);
        } else {
            System.out.println("✅ The rol already exists: " + roleName);
        }
    }

}
