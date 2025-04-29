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

    private BigDecimal finalPrice;

    private String status;

    private LocalDateTime orderedOn;

    private String promoCodeName;

    private BigDecimal discountPercent;

    private List<OrderItem> orderItems;

    public MakeOrderEvent(Object source, String fullName, String email, String deliveryAddress, String phoneNumber,
                          BigDecimal totalPrice, BigDecimal discount, BigDecimal finalPrice, String status,
                          LocalDateTime orderedOn, String promoCodeName, BigDecimal discountPercent, List<OrderItem> orderItems) {
        super(source);
        this.fullName = fullName;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.status = status;
        this.orderedOn = orderedOn;
        this.promoCodeName = promoCodeName;
        this.discountPercent = discountPercent;
        this.orderItems = orderItems;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
