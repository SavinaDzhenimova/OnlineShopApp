package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.Positive;

public class AddCartItemDTO {

    @Positive
    private Long productId;

    @Positive
    private Integer quantity;

    private Integer size;

    public AddCartItemDTO() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}