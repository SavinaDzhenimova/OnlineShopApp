package org.onlineshop.model.importDTO;

import java.math.BigDecimal;

public class OrderItemRequestDTO {

    private String name;

    private String imageUrl;

    private String category;

    private Integer selectedQuantity;

    private Integer selectedSize;

    private BigDecimal unitPrice;

    public OrderItemRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
