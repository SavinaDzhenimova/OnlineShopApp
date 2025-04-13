package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.User;
import org.onlineshop.model.user.UserDTO;

import java.util.Optional;

public interface UserService {
    void updateUserInfo(UserDTO userDTO);

    void refreshAuthentication(String newEmail);

    UserDTO getLoggedUserInfoForProfilePage();

    User getLoggedUser();

    Optional<User> getUserByEmail(String email);
}
