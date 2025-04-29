package org.onlineshop.service;

import org.onlineshop.model.entity.Role;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.user.UserDetailsDTO;
import org.onlineshop.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return this.userRepository
                .findByEmail(email)
                .map(UserDetailsServiceImpl::map)
                .orElseThrow(() -> new UsernameNotFoundException("Няма такъв потребител: " + email));
    }

    private static UserDetails map(User user) {

        return new UserDetailsDTO(
                user.getEmail(),
                user.getPassword(),
                mapAuthorities(user.getRole()),
                user.getId(),
                user.getFullName(),
                user.getAddresses(),
                user.getPhoneNumber(),
                user.getDiscountCard(),
                user.getTotalOutcome(),
                user.getOrders(),
                user.getRole(),
                user.getShoppingCart()
        );
    }

    private static Collection<? extends GrantedAuthority> mapAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
    }
}