package org.onlineshop.model.importDTO;

import java.math.BigDecimal;

public class AddOrderItemDTO {

    private int selectedQuantity;

    private int selectedSize;

    private BigDecimal unitPrice;

    public AddOrderItemDTO() {
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public int getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(int selectedSize) {
        this.selectedSize = selectedSize;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}