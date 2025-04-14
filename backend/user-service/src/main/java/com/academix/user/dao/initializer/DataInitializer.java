package com.academix.user.dao.initializer;

import com.academix.user.dao.Role;
import com.academix.user.dao.RoleEnum;
import com.academix.user.dao.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(RoleEnum.values()).forEach(role -> {
            Role byName = roleRepository.findByName(role);
            if (byName == null) {
                Role newRole = new Role();
                newRole.setName(role);
                newRole.setDescription("Role " + role);
                roleRepository.save(newRole);
            }
        });
        System.out.println("Roles initialized!");
    }
}
