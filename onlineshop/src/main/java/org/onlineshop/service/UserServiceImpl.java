package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.Role;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.enums.RoleName;
import org.onlineshop.model.user.UserDTO;
import org.onlineshop.model.user.UserRegisterDTO;
import org.onlineshop.repository.UserRepository;
import org.onlineshop.service.interfaces.*;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ShoppingCartServiceLogged shoppingCartService;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserProvider currentUserProvider;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, UserDetailsServiceImpl userDetailsService,
                           ShoppingCartServiceLogged shoppingCartService, PasswordResetService passwordResetService,
                           EmailService emailService, PasswordEncoder passwordEncoder,
                           CurrentUserProvider currentUserProvider) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
        this.shoppingCartService = shoppingCartService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Result registerUser(UserRegisterDTO userRegisterDTO) {

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            return new Result(false, "Паролите не съвпадат!");
        }

        Optional<Role> optionalRole = this.roleService.findRoleByRoleName(RoleName.CUSTOMER);
        Optional<User> optionalUserByEmail = this.userRepository.findByEmail(userRegisterDTO.getEmail());
        Optional<User> optionalUserByPhoneNumber = this.userRepository.findByPhoneNumber(userRegisterDTO.getPhoneNumber());

        if (optionalRole.isEmpty()) {
            return new Result(false, "Ролята, която искате да назначите на този потребител, не съществува!");
        }

        if (optionalUserByEmail.isPresent()) {
            return new Result(false, "Вече съществува потребител с този имейл!");
        }

        if (optionalUserByPhoneNumber.isPresent()) {
            return new Result(false, "Вече съществува потребител с този мобилен телефон!");
        }

        User user = new User();

        user.setFullName(userRegisterDTO.getFullName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPhoneNumber(userRegisterDTO.getPhoneNumber());
        user.setTotalOutcome(BigDecimal.ZERO);
        user.setAddress(userRegisterDTO.getAddress());
        user.setPassword(this.passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRole(optionalRole.get());
        user.setOrders(new HashSet<>());

        this.userRepository.saveAndFlush(user);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        this.shoppingCartService.saveAndFlush(shoppingCart);

        user.setShoppingCart(shoppingCart);

        if (!userRegisterDTO.isPrivacyPolicy()) {
            return new Result(false, "Трябва да се съгласите с Политиката за поверителност!");
        }

        this.userRepository.saveAndFlush(user);

        Optional<User> optionalUserAfterRegistration = this.getUserByEmail(userRegisterDTO.getEmail());

        if (optionalUserAfterRegistration.isEmpty()) {
            return new Result(false, "Нещо се обърка! Не можахме да Ви регистрираме!");
        }

        return new Result(true, "Успешно се регистрирахте!");
    }

    @Override
    public void updateUserInfo(UserDTO userDTO) {

        User loggedUser = this.currentUserProvider.getLoggedUser();

        Optional<User> optionalUserByEmail = this.getUserByEmail(userDTO.getEmail());

        if (!loggedUser.getEmail().equals(userDTO.getEmail()) && optionalUserByEmail.isEmpty()) {
            loggedUser.setEmail(userDTO.getEmail());
        }

        if (!loggedUser.getFullName().equals(userDTO.getFullName())) {
            loggedUser.setFullName(userDTO.getFullName());
        }

        if (!loggedUser.getPhoneNumber().equals(userDTO.getPhoneNumber())) {
            loggedUser.setPhoneNumber(userDTO.getPhoneNumber());
        }

        this.userRepository.saveAndFlush(loggedUser);
    }

    @Override
    public Result resetPassword(String password, String confirmPassword, String token) {

        Optional<User> optionalUser = this.passwordResetService.validateToken(token);

        if (optionalUser.isEmpty()) {
            return new Result(false, "Паролите не съвпадат!");
        }

        User user = optionalUser.get();

        user.setPassword(this.passwordEncoder.encode(password));

        this.userRepository.saveAndFlush(user);

        return new Result(true, "Успешно променихте своята парола!");
    }

    @Override
    public Result sendEmailForForgottenPassword(String email) {

        Optional<User> optionalUser = this.getUserByEmail(email);

        if (optionalUser.isEmpty()) {
            return new Result(false, "Не открихме потребител с посочения имейл!");
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();
        this.passwordResetService.createTokenForUser(user, token);

        this.emailService.sendForgotPasswordEmail(user.getFullName(), user.getEmail(), token);

        return new Result(true, "Моля проверете пощата си за имейл с линк за смяна на паролата!");
    }

    @Override
    public void refreshAuthentication(String newEmail) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(newEmail);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public UserDTO getLoggedUserInfoForProfilePage() {

        User loggedUser = this.currentUserProvider.getLoggedUser();

        UserDTO userDTO = new UserDTO();

        userDTO.setFullName(loggedUser.getFullName());
        userDTO.setEmail(loggedUser.getEmail());
        userDTO.setPhoneNumber(loggedUser.getPhoneNumber());

        return userDTO;
    }

    @Override
    public ShoppingCart getLoggedUserShoppingCart(HttpSession session) {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            return loggedUser.getShoppingCart();
        } else {
            ShoppingCart guestCart = (ShoppingCart) session.getAttribute("guestCart");

            if (guestCart == null) {
                guestCart = new ShoppingCart();
                guestCart.setCartItems(new ArrayList<>());
                session.setAttribute("guestCart", guestCart);
            }

            return guestCart;
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
