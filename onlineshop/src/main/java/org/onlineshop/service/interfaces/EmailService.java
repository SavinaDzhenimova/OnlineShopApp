package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {

    void sendForgotPasswordEmail(String fullName, String email, String token);

    void sendUpdateOrderStatusEmail(Long id, String fullName, String email, String previousStatus, String currentStatus);

    void sendMakeOrderEmail(String fullName, String email, String deliveryAddress, String phoneNumber,
                            BigDecimal totalPrice, BigDecimal discount, BigDecimal finalPrice, String status,
                            LocalDateTime orderedOn, String orderTrackingUrl);
}