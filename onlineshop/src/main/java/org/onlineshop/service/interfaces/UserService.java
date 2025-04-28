package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.exportDTO.OrderDTO;
import org.onlineshop.model.exportDTO.VipStatusDTO;
import org.onlineshop.model.user.UserDTO;
import org.onlineshop.model.user.UserRegisterDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void updateUserInfo(UserDTO userDTO);

    Result resetPassword(String password, String confirmPassword, String token);

    Result sendEmailForForgottenPassword(String email);

    void refreshAuthentication(String newEmail);

    UserDTO getLoggedUserInfoForProfilePage();

    List<OrderDTO> getLoggedUserOrders();

    Optional<User> getUserByEmail(String email);

    Result registerUser(UserRegisterDTO userRegisterDTO);

    VipStatusDTO calculateVipStatus();

    ShoppingCart getLoggedUserShoppingCart(HttpSession session);
}
