package org.onlineshop.model.importDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddOrderDTO {

    private String fullName;

    private String email;

    private String phoneNumber;

    private String address;

    private BigDecimal totalPrice;

    private BigDecimal discount;

    private BigDecimal finalPrice;

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

    public List<AddOrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<AddOrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
