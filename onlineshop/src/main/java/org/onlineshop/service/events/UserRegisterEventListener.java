package org.onlineshop.service.events;

import org.onlineshop.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterEventListener {

    private final EmailService emailService;

    public UserRegisterEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleUserRegisterEvent(UserRegisterEvent userRegisterEvent) {

        this.emailService.sendUserRegisterEmail(userRegisterEvent.getFullName(), userRegisterEvent.getEmail(),
                userRegisterEvent.getPhoneNumber());
    }
}
