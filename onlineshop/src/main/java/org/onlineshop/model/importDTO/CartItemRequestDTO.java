package org.onlineshop.model.importDTO;

import java.math.BigDecimal;

public class CartItemRequestDTO {

    private Long id;

    private Integer selectedQuantity;

    private Integer selectedSize;

    private BigDecimal unitPrice;

    public CartItemRequestDTO() {
    }

    public CartItemRequestDTO(Integer selectedQuantity, Integer selectedSize, BigDecimal unitPrice) {
        this.selectedQuantity = selectedQuantity;
        this.selectedSize = selectedSize;
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public Integer getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(Integer selectedSize) {
        this.selectedSize = selectedSize;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
