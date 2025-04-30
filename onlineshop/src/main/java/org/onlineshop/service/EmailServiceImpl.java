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
    public void sendForgotPasswordEmail(String fullName, String email, String token) {
        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "email", email,
                "token", token
        );

        String content = generateEmailContent("/email/forgot-password-email", variables);
        sendEmail(email, "Линк за промяна на парола", content);
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
        sendEmail(email, "Променен статус на поръчка", content);
    }

    @Override
    public void sendMakeOrderEmail(String fullName, String email, String deliveryAddress, String phoneNumber,
                                   BigDecimal totalPrice, BigDecimal discount, BigDecimal finalPrice, String status,
                                   LocalDateTime orderedOn, String orderTrackingUrl) {
        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "email", email,
                "deliveryAddress", deliveryAddress,
                "phoneNumber", phoneNumber,
                "totalPrice", totalPrice,
                "discount", discount,
                "finalPrice", finalPrice,
                "status", status,
                "orderedOn", orderedOn,
                "orderTrackingUrl", orderTrackingUrl
        );

        String content = generateEmailContent("/email/make-order-email", variables);
        sendEmail(email, "Успешно направена поръчка", content);
    }

    private void sendEmail(String sendTo, String subject, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setFrom(this.email);
            mimeMessageHelper.setReplyTo(this.email);
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
