package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.annotations.ValidEmail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddOrderDTO {

    @NotNull
    @Size(min = 5, max = 50)
    private String fullName;

    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @NotNull
    @Size(min = 10, max = 70)
    private String address;

    @NotNull
    @Positive
    private BigDecimal totalPrice;

    private String promoCode;

    private BigDecimal discountPercent;

    private BigDecimal discount;

    private BigDecimal vipStatusDiscount;

    private BigDecimal finalPrice;

    @NotEmpty
    private List<AddOrderItemDTO> orderItems;

    public AddOrderDTO() {
        this.orderItems = new ArrayList<>();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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

    public List<AddOrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<AddOrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
