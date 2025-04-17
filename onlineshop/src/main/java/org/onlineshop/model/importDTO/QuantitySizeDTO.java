package org.onlineshop.model.importDTO;

import org.onlineshop.model.entity.ShoeSize;

public class QuantitySizeDTO {

    private Integer size;

    private int quantity;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
