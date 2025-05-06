package org.onlineshop.service.events;

import org.onlineshop.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SubscribeEventListener {

    private final EmailService emailService;

    public SubscribeEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleSubscribeEvent(SubscribeEvent subscribeEvent) {

        this.emailService.sendSubscribeEmail(subscribeEvent.getEmail());
    }
}