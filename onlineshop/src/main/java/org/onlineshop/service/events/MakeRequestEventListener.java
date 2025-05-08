package org.onlineshop.service.events;

import org.onlineshop.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MakeRequestEventListener {

    private final EmailService emailService;

    public MakeRequestEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleForgotPasswordEvent(MakeRequestEvent makeRequestEvent) {

        this.emailService.sendMakeRequestEmail(makeRequestEvent.getFullName(), makeRequestEvent.getEmail(),
                makeRequestEvent.getPhoneNumber(), makeRequestEvent.getAddress(), makeRequestEvent.getRequestType());
    }
}
