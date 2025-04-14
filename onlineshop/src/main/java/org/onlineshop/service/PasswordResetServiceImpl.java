package org.onlineshop.service;

import jakarta.transaction.Transactional;
import org.onlineshop.model.entity.PasswordResetToken;
import org.onlineshop.model.entity.User;
import org.onlineshop.repository.PasswordResetTokenRepository;
import org.onlineshop.service.interfaces.PasswordResetService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetServiceImpl(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void createTokenForUser(User user, String token) {

        PasswordResetToken resetToken = new PasswordResetToken();

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        this.tokenRepository.saveAndFlush(resetToken);
    }

    @Override
    public Optional<User> validateToken(String token) {

        return this.tokenRepository.findByToken(token)
                .filter(resetToken ->
                        !resetToken.isUsed() && resetToken.getExpiryDate().isAfter(LocalDateTime.now()))
                .map(PasswordResetToken::getUser);
    }

    @Override
    @Transactional
    public void markTokenAsUsed(String token) {

        this.tokenRepository.findByToken(token).ifPresent(resetToken -> {
            resetToken.setUsed(true);
            this.tokenRepository.saveAndFlush(resetToken);
        });

        this.tokenRepository.deleteByToken(token);
    }
}
