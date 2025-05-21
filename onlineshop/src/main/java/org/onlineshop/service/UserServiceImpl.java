package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.RoleName;
import org.onlineshop.model.exportDTO.*;
import org.onlineshop.model.importDTO.AddAddressDTO;
import org.onlineshop.model.user.UserDTO;
import org.onlineshop.model.user.UserRegisterDTO;
import org.onlineshop.repository.UserRepository;
import org.onlineshop.service.events.ForgotPasswordEvent;
import org.onlineshop.service.events.UserRegisterEvent;
import org.onlineshop.service.interfaces.*;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ShoppingCartServiceLogged shoppingCartService;
    private final OrderService orderService;
    private final AddressService addressService;
    private final ProductService productService;
    private final SubscriberService subscriberService;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserProvider currentUserProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, UserDetailsServiceImpl userDetailsService,
                           ShoppingCartServiceLogged shoppingCartService, OrderService orderService,
                           AddressService addressService, ProductService productService,
                           SubscriberService subscriberService, PasswordResetService passwordResetService,
                           PasswordEncoder passwordEncoder, CurrentUserProvider currentUserProvider,
                           ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.addressService = addressService;
        this.productService = productService;
        this.subscriberService = subscriberService;
        this.passwordResetService = passwordResetService;
        this.passwordEncoder = passwordEncoder;
        this.currentUserProvider = currentUserProvider;
        this.applicationEventPublisher = applicationEventPublisher;
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
        user.setPassword(this.passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRole(optionalRole.get());
        user.setOrders(new HashSet<>());

        this.userRepository.saveAndFlush(user);

        Address address = new Address();

        address.setRegion(userRegisterDTO.getRegion());
        address.setAddressType(AddressType.PERSONAL);
        address.setTown(userRegisterDTO.getTown());
        address.setPostalCode(userRegisterDTO.getPostalCode());
        address.setStreet(userRegisterDTO.getStreet());
        address.setUser(user);

        user.getAddresses().add(address);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        this.shoppingCartService.saveAndFlush(shoppingCart);

        user.setShoppingCart(shoppingCart);

        if (!userRegisterDTO.isPrivacyPolicy()) {
            return new Result(false, "Трябва да се съгласите с Политиката за поверителност!");
        }

        if (userRegisterDTO.isNewsletter()) {
            Optional<Subscriber> optionalSubscriber = this.subscriberService.getByEmail(userRegisterDTO.getEmail());

            if (optionalSubscriber.isEmpty()) {
                Subscriber subscriber = new Subscriber();
                subscriber.setEmail(userRegisterDTO.getEmail());

                this.subscriberService.saveAndFlush(subscriber);
            }
        }

        this.userRepository.saveAndFlush(user);

        Optional<User> optionalUserAfterRegistration = this.getUserByEmail(userRegisterDTO.getEmail());

        if (optionalUserAfterRegistration.isEmpty()) {
            return new Result(false, "Нещо се обърка! Не можахме да Ви регистрираме!");
        }

        this.applicationEventPublisher.publishEvent(
                new UserRegisterEvent(this, user.getFullName(), user.getEmail(), user.getPhoneNumber()));

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

        this.applicationEventPublisher.publishEvent(
                new ForgotPasswordEvent(this, user.getFullName(), user.getEmail(), token));

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
    public VipStatusDTO calculateVipStatus() {

        BigDecimal totalOutcome = this.currentUserProvider.getLoggedUser().getTotalOutcome();
        BigDecimal amountToNextLevel = null;
        String nextLevel = "";
        String message = null;

        if (totalOutcome.compareTo(BigDecimal.valueOf(1201)) >= 0) {
            message = "Поздравления! Вие сте достигнали ниво ВИП 1200!";
        } else if (totalOutcome.compareTo(BigDecimal.valueOf(701)) >= 0) {
            nextLevel = "ВИП 1200";
            amountToNextLevel = BigDecimal.valueOf(1200).subtract(totalOutcome);
        } else if (totalOutcome.compareTo(BigDecimal.valueOf(300)) >= 0) {
            nextLevel = "ВИП 700";
            amountToNextLevel = BigDecimal.valueOf(700).subtract(totalOutcome);
        } else {
            nextLevel = "ВИП 300";
            amountToNextLevel = BigDecimal.valueOf(300).subtract(totalOutcome);
        }

        VipStatusDTO vipStatusDTO = new VipStatusDTO();

        vipStatusDTO.setNextLevel(nextLevel);
        vipStatusDTO.setAmountToNextLevel(amountToNextLevel);
        vipStatusDTO.setMessage(message);

        return vipStatusDTO;
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
    public List<OrderDTO> getLoggedUserOrders() {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        return loggedUser.getOrders().stream()
                .map(this.orderService::mapOrderToDto)
                .toList();
    }

    @Override
    public List<AddressDTO> getLoggedUserAddresses() {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        return loggedUser.getAddresses().stream()
                .map(address -> {
                    AddressDTO addressDTO = new AddressDTO();

                    addressDTO.setId(address.getId());
                    addressDTO.setRegion(address.getRegion().getDisplayName());
                    addressDTO.setAddressType(address.getAddressType().getDisplayName());
                    addressDTO.setTown(address.getTown());
                    addressDTO.setPostalCode(address.getPostalCode());
                    addressDTO.setStreet(address.getStreet());

                    return addressDTO;
                }).toList();
    }

    @Override
    public Result addAddress(AddAddressDTO addAddressDTO) {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        Address address = new Address();

        address.setRegion(addAddressDTO.getRegion());
        address.setAddressType(addAddressDTO.getAddressType());
        address.setTown(addAddressDTO.getTown());
        address.setPostalCode(addAddressDTO.getPostalCode());
        address.setStreet(addAddressDTO.getStreet());
        address.setUser(loggedUser);

        this.addressService.saveAndFlush(address);

        loggedUser.getAddresses().add(address);
        this.userRepository.saveAndFlush(loggedUser);

        return new Result(true, "Успешно добавихте нов адрес!");
    }

    @Override
    public Result deleteAddress(Long id) {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        Optional<Address> optionalAddress = this.addressService.getById(id);

        if (optionalAddress.isEmpty()) {
            return new Result(false, "Адресът, който се опитвате да изтриете, не съществува!");
        }

        Address address = optionalAddress.get();

        if (!address.getUser().getEmail().equals(loggedUser.getEmail())) {
            return new Result(false, "Нямате права да изтриете този адрес!");
        }

        loggedUser.getAddresses().remove(address);
        this.userRepository.saveAndFlush(loggedUser);

        address.setUser(null);
        this.addressService.saveAndFlush(address);

        this.addressService.deleteById(id);

        Optional<Address> optionalAddressAfterDeletion = this.addressService.getById(id);

        if (optionalAddressAfterDeletion.isPresent()) {
            return new Result(false, "Адресът не можа да бъде изтрит!");
        }

        return new Result(true, "Успешно изтрихте избрания адрес!");
    }

    @Override
    public Page<ProductDTO> getFavouriteProducts(HttpSession session, Pageable pageable) {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        List<Product> favouriteProducts;

        if (loggedUser != null) {
            favouriteProducts = new ArrayList<>(loggedUser.getFavourites());
        } else {
            List<Long> favouriteIds = (List<Long>) session.getAttribute("guestFavourites");
            if (favouriteIds == null) {
                favouriteIds = new ArrayList<>();
            }

            favouriteProducts = favouriteIds.stream()
                    .map(id -> this.productService.getById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .toList();
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), favouriteProducts.size());

        List<ProductDTO> content = favouriteProducts.subList(start, end).stream()
                .map(this.productService::mapProductToDTO)
                .toList();

        return new PageImpl<>(content, pageable, favouriteProducts.size());
    }

    @Override
    public Result addProductToFavourites(Long id, HttpSession session) {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        Optional<Product> optionalProduct = this.productService.getById(id);

        if (optionalProduct.isEmpty()) {
            return new Result(false, "Продуктът, който искате да добавите в списъка Ви с любими, не съществува!");
        }

        if (loggedUser != null) {
            boolean alreadyInFavourites = loggedUser.getFavourites().stream()
                    .anyMatch(product -> product.getId().equals(id));

            if (alreadyInFavourites) {
                return new Result(false, "Продуктът вече е добавен в списъка Ви с любими!");
            }

            loggedUser.getFavourites().add(optionalProduct.get());
            this.userRepository.saveAndFlush(loggedUser);

        } else {
            List<Long> guestFavourites = (List<Long>) session.getAttribute("guestFavourites");

            if (guestFavourites == null) {
                guestFavourites = new ArrayList<>();
                session.setAttribute("guestFavourites", guestFavourites);
            }

            if (guestFavourites.contains(id)) {
                return new Result(false, "Продуктът вече е в списъка Ви с любими!");
            }

            guestFavourites.add(id);
        }

        return new Result(true, "Продуктът е добавен в списъка Ви с любими!");
    }

    @Override
    public Result removeProductFromFavourites(Long id, HttpSession session) {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            boolean removed = loggedUser.getFavourites()
                    .removeIf(product -> product.getId().equals(id));

            if (removed) {
                this.userRepository.saveAndFlush(loggedUser);

                return new Result(true, "Продуктът е премахнат от списъка Ви с любими.");
            }
        } else {
            List<Long> guestFavourites = (List<Long>) session.getAttribute("guestFavourites");

            if (guestFavourites != null && guestFavourites.remove(id)) {
                return new Result(true, "Продуктът е премахнат от списъка Ви с любими.");
            }
        }

        return new Result(false, "Продуктът не беше намерен.");
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void saveAndFlushUser(User user) {
        this.userRepository.saveAndFlush(user);
    }
}