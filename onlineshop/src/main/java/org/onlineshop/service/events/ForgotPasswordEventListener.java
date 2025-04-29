package org.onlineshop.service.events;

import org.onlineshop.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordEventListener {

    private final EmailService emailService;

    public ForgotPasswordEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleForgotPasswordEvent(ForgotPasswordEvent forgotPasswordEvent) {

        this.emailService.sendForgotPasswordEmail(forgotPasswordEvent.getFullName(), forgotPasswordEvent.getEmail(),
                forgotPasswordEvent.getToken());
    }
}