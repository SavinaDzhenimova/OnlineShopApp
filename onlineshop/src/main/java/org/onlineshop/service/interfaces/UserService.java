package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.user.UserDTO;
import org.onlineshop.model.user.UserRegisterDTO;

import java.util.Optional;

public interface UserService {
    void updateUserInfo(UserDTO userDTO);

    Result resetPassword(String password, String confirmPassword, String token);

    Result sendEmailForForgottenPassword(String email);

    void refreshAuthentication(String newEmail);

    UserDTO getLoggedUserInfoForProfilePage();

    Optional<User> getUserByEmail(String email);

    Result registerUser(UserRegisterDTO userRegisterDTO);

    ShoppingCart getLoggedUserShoppingCart(HttpSession session);
}
