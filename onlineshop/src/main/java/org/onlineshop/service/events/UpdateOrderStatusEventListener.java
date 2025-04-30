package org.onlineshop.service.events;

import org.onlineshop.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderStatusEventListener {

    private final EmailService emailService;

    public UpdateOrderStatusEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleForgotPasswordEvent(UpdateOrderStatusEvent updateOrderStatusEvent) {

        this.emailService.sendUpdateOrderStatusEmail(updateOrderStatusEvent.getId(),
                updateOrderStatusEvent.getFullName(), updateOrderStatusEvent.getEmail(),
                updateOrderStatusEvent.getPreviousStatus(), updateOrderStatusEvent.getCurrentStatus());
    }
}