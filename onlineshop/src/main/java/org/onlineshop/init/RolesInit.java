package org.onlineshop.init;

import org.onlineshop.model.entity.Role;
import org.onlineshop.model.enums.RoleName;
import org.onlineshop.repository.RoleRepository;
import org.onlineshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order(1)
public class RolesInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RolesInit(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.roleRepository.count() == 0) {

            Arrays.stream(RoleName.values())
                    .forEach(roleName -> {
                        Role role = new Role();
                        role.setRoleName(roleName);

                        String description = switch (roleName) {
                            case ADMIN ->
                                    "Админът може да се логва, да добавя продукти, да изтрива продукти, " +
                                            "да променя ролите на клиентите, да разглежда, одобрява и изтрива отзиви.";
                            case CUSTOMER ->
                                    "Клиентът може да се логва, да се регистрира, да прави поръчки, да пише отзиви.";
                            case EMPLOYEE -> "Работникът може да се логва, да разглежда отзиви и да добавя и изтрива продукти.";
                        };

                        role.setDescription(description);
                        this.roleRepository.saveAndFlush(role);
                    });
        }
    }
}