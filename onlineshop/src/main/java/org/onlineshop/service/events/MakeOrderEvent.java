package org.onlineshop.service.events;

import org.onlineshop.model.entity.OrderItem;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MakeOrderEvent extends ApplicationEvent {

    private String fullName;

    private String email;

    private String deliveryAddress;

    private String phoneNumber;

    private BigDecimal totalPrice;

    private BigDecimal discount;

    private String promoCodeName;

    private BigDecimal discountPercent;

    private BigDecimal finalPrice;

    private String status;

    private LocalDateTime orderedOn;

    private String discountCardName;

    private BigDecimal discountCardPercent;

    private BigDecimal vipStatusDiscount;

    private String orderTrackingUrl;

    public MakeOrderEvent(Object source, String fullName, String email, String deliveryAddress, String phoneNumber,
                          BigDecimal totalPrice, BigDecimal discount, String promoCodeName, BigDecimal discountPercent,
                          BigDecimal finalPrice, String status, LocalDateTime orderedOn, String discountCardName,
                          BigDecimal discountCardPercent, BigDecimal vipStatusDiscount, String orderTrackingUrl) {
        super(source);
        this.fullName = fullName;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.promoCodeName = promoCodeName;
        this.discountPercent = discountPercent;
        this.finalPrice = finalPrice;
        this.status = status;
        this.orderedOn = orderedOn;
        this.discountCardName = discountCardName;
        this.discountCardPercent = discountCardPercent;
        this.vipStatusDiscount = vipStatusDiscount;
        this.orderTrackingUrl = orderTrackingUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getPromoCodeName() {
        return promoCodeName;
    }

    public void setPromoCodeName(String promoCodeName) {
        this.promoCodeName = promoCodeName;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }

    public String getDiscountCardName() {
        return discountCardName;
    }

    public void setDiscountCardName(String discountCardName) {
        this.discountCardName = discountCardName;
    }

    public BigDecimal getDiscountCardPercent() {
        return discountCardPercent;
    }

    public void setDiscountCardPercent(BigDecimal discountCardPercent) {
        this.discountCardPercent = discountCardPercent;
    }

    public BigDecimal getVipStatusDiscount() {
        return vipStatusDiscount;
    }

    public void setVipStatusDiscount(BigDecimal vipStatusDiscount) {
        this.vipStatusDiscount = vipStatusDiscount;
    }

    public String getOrderTrackingUrl() {
        return orderTrackingUrl;
    }

    public void setOrderTrackingUrl(String orderTrackingUrl) {
        this.orderTrackingUrl = orderTrackingUrl;
    }
}
