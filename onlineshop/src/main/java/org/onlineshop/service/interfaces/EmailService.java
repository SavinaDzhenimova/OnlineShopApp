package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {

    void sendForgotPasswordEmail(String fullName, String email, String token);

    void sendUserRegisterEmail(String fullName, String email, String phoneNumber);

    void sendMakeRequestEmail(String fullName, String email, String phoneNumber, String address, String requestType);

    void sendSubscribeEmail(String email);

    void sendUpdateOrderStatusEmail(Long id, String fullName, String email, String previousStatus, String currentStatus);

    void sendMakeOrderEmail(String fullName, String email, String deliveryAddress, String phoneNumber,
                            BigDecimal totalPrice, BigDecimal discount, String promoCodeName, BigDecimal discountPercent,
                            BigDecimal finalPrice, String status, LocalDateTime orderedOn, String discountCardName,
                            BigDecimal discountCardPercent, BigDecimal vipStatusDiscount, String orderTrackingUrl);
}