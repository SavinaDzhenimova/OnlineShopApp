package org.onlineshop.init;

import org.onlineshop.model.entity.Address;
import org.onlineshop.model.entity.Role;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.Region;
import org.onlineshop.model.enums.RoleName;
import org.onlineshop.repository.AddressRepository;
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
    private final AddressRepository addressRepository;

    public AdminInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                     ShoppingCartRepository shoppingCartRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.shoppingCartRepository = shoppingCartRepository;
        this.addressRepository = addressRepository;
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

            Address address = new Address();

            address.setRegion(Region.SMOLYAN);
            address.setAddressType(AddressType.PERSONAL);
            address.setTown("Мадан");
            address.setPostalCode("4900");
            address.setStreet("Република 15");

            user.getAddresses().add(address);
            user.setPassword(this.passwordEncoder.encode("Admin1234"));
            user.setOrders(new HashSet<>());
            user.setRole(role);
            user.setTotalOutcome(BigDecimal.ZERO);

            this.userRepository.saveAndFlush(user);

            address.setUser(user);
            this.addressRepository.saveAndFlush(address);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            this.shoppingCartRepository.saveAndFlush(shoppingCart);

            user.setShoppingCart(shoppingCart);
            this.userRepository.saveAndFlush(user);
        }
    }
}