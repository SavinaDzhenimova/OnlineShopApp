package org.onlineshop.init;

import org.onlineshop.model.entity.Role;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.enums.RoleName;
import org.onlineshop.repository.RoleRepository;
import org.onlineshop.repository.ShoppingCartRepository;
import org.onlineshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@Component
@Order(2)
public class AdminInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartRepository shoppingCartRepository;

    public AdminInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                     ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void run(String... args) {
        if (this.userRepository.count() == 0) {

            User user = new User();
            Optional<Role> optionalAdmin = this.roleRepository.findByRoleName(RoleName.ADMIN);
            Role role = optionalAdmin.get();

            user.setFullName("Admin One");
            user.setPhoneNumber("0895121616");
            user.setEmail("runtastic.shoes.contacts@gmail.com");
            user.setAddress("Str. Republika 15, 4900 Madan, Smolyan");
            user.setPassword(this.passwordEncoder.encode("Admin1234"));
            user.setOrders(new HashSet<>());
            user.setRole(role);
            user.setTotalOutcome(BigDecimal.ZERO);

            this.userRepository.saveAndFlush(user);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            this.shoppingCartRepository.saveAndFlush(shoppingCart);

            user.setShoppingCart(shoppingCart);
            this.userRepository.saveAndFlush(user);
        }
    }
}