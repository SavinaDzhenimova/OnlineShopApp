package org.onlineshop.service.interfaces;

public interface EmailService {

    void sendForgotPasswordEmail(String fullName, String email, String token);
}
