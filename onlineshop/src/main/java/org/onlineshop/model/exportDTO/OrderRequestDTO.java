package org.onlineshop.model.exportDTO;

import org.onlineshop.model.importDTO.OrderItemRequestDTO;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequestDTO {

    private List<OrderItemRequestDTO> orderItems;

    private String promoCode;

    private BigDecimal totalPrice;

    private BigDecimal discountPercent;

    private BigDecimal discount;

    private BigDecimal finalPrice;

    public OrderRequestDTO() {
    }

    public List<OrderItemRequestDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemRequestDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
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

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }
}
