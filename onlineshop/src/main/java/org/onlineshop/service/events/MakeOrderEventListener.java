package org.onlineshop.service.events;

import org.onlineshop.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MakeOrderEventListener {

    private final EmailService emailService;

    public MakeOrderEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleForgotPasswordEvent(MakeOrderEvent makeOrderEvent) {

        this.emailService.sendMakeOrderEmail(makeOrderEvent.getFullName(), makeOrderEvent.getEmail(),
                makeOrderEvent.getDeliveryAddress(), makeOrderEvent.getPhoneNumber(), makeOrderEvent.getTotalPrice(),
                makeOrderEvent.getDiscount(), makeOrderEvent.getFinalPrice(), makeOrderEvent.getStatus(),
                makeOrderEvent.getOrderedOn(), makeOrderEvent.getPromoCodeName(), makeOrderEvent.getDiscountPercent(),
                makeOrderEvent.getOrderItems());
    }
}