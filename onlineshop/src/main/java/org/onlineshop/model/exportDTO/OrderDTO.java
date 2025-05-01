package org.onlineshop.model.exportDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String deliveryAddress;

    private LocalDateTime orderedOn;

    private String status;

    private String statusClass;

    private BigDecimal totalPrice;

    private BigDecimal discount;

    private BigDecimal discountPercent;

    private BigDecimal finalPrice;

    private String promoCode;

    private List<OrderItemDTO> orderItems;

    private String discountCardName;

    private BigDecimal discountCardPercent;

    private BigDecimal vipStatusDiscount;

    public OrderDTO() {
        this.orderItems = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusClass() {
        return statusClass;
    }

    public void setStatusClass(String statusClass) {
        this.statusClass = statusClass;
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

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
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

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
