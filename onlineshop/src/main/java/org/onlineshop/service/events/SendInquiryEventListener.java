package org.onlineshop.service.events;

import org.onlineshop.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SendInquiryEventListener {

    private final EmailService emailService;

    public SendInquiryEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleSubscribeEvent(SendInquiryEvent sendInquiryEvent) {

        this.emailService.sendInquiryEmail(sendInquiryEvent.getFullName(), sendInquiryEvent.getEmail(),
                sendInquiryEvent.getPhoneNumber(), sendInquiryEvent.getMessage());
    }
}