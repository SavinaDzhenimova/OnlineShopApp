package org.onlineshop.model.importDTO;

import org.onlineshop.model.enums.Size;

public class QuantitySizeDTO {

    private Size size;

    private int quantity;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
