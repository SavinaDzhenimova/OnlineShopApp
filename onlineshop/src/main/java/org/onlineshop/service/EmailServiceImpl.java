package org.onlineshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.onlineshop.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String email;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender javaMailSender,
                            @Value("${mail.online_shop}") String email) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.email = email;
    }

    @Override
    public void sendInquiryEmail(String fullName, String email, String phoneNumber, String message) {
        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "email", email,
                "phoneNumber", phoneNumber,
                "message", message
        );

        String content = generateEmailContent("/email/inquiry-email", variables);
        sendEmail("runtastic.shoes.contacts@gmail.com", "Ново запитване от " + fullName, content, email);
    }

    @Override
    public void sendForgotPasswordEmail(String fullName, String email, String token) {
        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "email", email,
                "token", token
        );

        String content = generateEmailContent("/email/forgot-password-email", variables);
        sendEmail(email, "Линк за промяна на парола", content, this.email);
    }

    @Override
    public void sendUserRegisterEmail(String fullName, String email, String phoneNumber) {
        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "email", email,
                "phoneNumber", phoneNumber
        );

        String content = generateEmailContent("/email/user-register-email", variables);
        sendEmail(email, "Успешна регистрация в Runtastic Shoes", content, this.email);
    }

    @Override
    public void sendMakeRequestEmail(String fullName, String email, String phoneNumber, String address, String requestType) {
        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "email", email,
                "phoneNumber", phoneNumber,
                "address", address,
                "requestType", requestType
        );

        String content = generateEmailContent("/email/make-request-email", variables);
        sendEmail(email, "Заявка за " + requestType, content, this.email);
    }

    @Override
    public void sendSubscribeEmail(String email) {
        Map<String, Object> variables = Map.of(
                "email", email
        );

        String content = generateEmailContent("/email/subscribe-email", variables);
        sendEmail(email, "Успешен абонамент", content, this.email);
    }

    @Override
    public void sendUpdateOrderStatusEmail(Long id, String fullName, String email, String previousStatus, String currentStatus) {
        Map<String, Object> variables = Map.of(
                "id", id,
                "fullName", fullName,
                "email", email,
                "previousStatus", previousStatus,
                "currentStatus", currentStatus
        );

        String content = generateEmailContent("/email/update-order-status-email", variables);
        sendEmail(email, "Променен статус на поръчка", content, this.email);
    }

    @Override
    public void sendMakeOrderEmail(String fullName, String email, String deliveryAddress, String phoneNumber,
                                   BigDecimal totalPrice, BigDecimal discount, String promoCodeName, BigDecimal discountPercent,
                                   BigDecimal finalPrice, String status, LocalDateTime orderedOn, String discountCardName,
                                   BigDecimal discountCardPercent, BigDecimal vipStatusDiscount, String orderTrackingUrl) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("fullName", fullName);
        variables.put("email", email);
        variables.put("deliveryAddress", deliveryAddress);
        variables.put("phoneNumber", phoneNumber);
        variables.put("totalPrice", totalPrice);
        variables.put("discount", discount);
        variables.put("promoCodeName", promoCodeName);
        variables.put("discountPercent", discountPercent);
        variables.put("finalPrice", finalPrice);
        variables.put("status", status);
        variables.put("orderedOn", orderedOn);
        variables.put("discountCardName", discountCardName);
        variables.put("discountCardPercent", discountCardPercent);
        variables.put("vipStatusDiscount", vipStatusDiscount);
        variables.put("orderTrackingUrl", orderTrackingUrl);

        String content = generateEmailContent("/email/make-order-email", variables);
        sendEmail(email, "Успешно направена поръчка", content, this.email);
    }

    private void sendEmail(String sendTo, String subject, String content, String replyTo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setFrom(this.email);
            mimeMessageHelper.setReplyTo(replyTo);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Грешка при изпращане на имейл: " + e.getMessage(), e);
        }
    }

    private String generateEmailContent(String templatePath, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templatePath, context);
    }
}
