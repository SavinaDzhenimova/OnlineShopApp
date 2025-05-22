package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.exportDTO.*;
import org.onlineshop.model.importDTO.AddAddressDTO;
import org.onlineshop.model.user.UserDTO;
import org.onlineshop.model.user.UserRegisterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void updateUserInfo(UserDTO userDTO);

    Result resetPassword(String password, String confirmPassword, String token);

    Result sendEmailForForgottenPassword(String email);

    void refreshAuthentication(String newEmail);

    UserDTO getLoggedUserInfoForProfilePage();

    Optional<User> getUserByEmail(String email);

    Result registerUser(UserRegisterDTO userRegisterDTO);

    VipStatusDTO calculateVipStatus();

    ShoppingCart getLoggedUserShoppingCart(HttpSession session);

    Page<OrderDTO> getLoggedUserOrders(Pageable pageable);

    List<AddressDTO> getLoggedUserAddresses();

    Result deleteAddress(Long id);

    Result addAddress(AddAddressDTO addAddressDTO);

    void saveAndFlushUser(User user);

    Page<ProductDTO> getFavouriteProducts(HttpSession session, Pageable pageable);

    Result addProductToFavourites(Long id, HttpSession session);

    Result removeProductFromFavourites(Long id, HttpSession session);
}