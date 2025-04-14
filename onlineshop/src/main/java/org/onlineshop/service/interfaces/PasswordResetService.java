package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.User;

import java.util.Optional;

public interface PasswordResetService {
    void createTokenForUser(User user, String token);

    Optional<User> validateToken(String token);

    void markTokenAsUsed(String token);
}
