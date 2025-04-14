package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
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

    User getLoggedUser();

    Optional<User> getUserByEmail(String email);

    Result registerUser(UserRegisterDTO userRegisterDTO);

}
