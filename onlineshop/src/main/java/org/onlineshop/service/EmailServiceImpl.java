package org.onlineshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.onlineshop.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

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
    public void sendForgotPasswordEmail(String fullName, String email, String password) {
        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "email", email,
                "password", password
        );

        String content = generateEmailContent("/email/forgot-password-email", variables);
        sendEmail(email, "Временна парола за достъп до профила ви", content);
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
