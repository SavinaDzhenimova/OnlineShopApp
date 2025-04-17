package org.onlineshop.model.importDTO;

public class QuantitySizeDTO implements Comparable<QuantitySizeDTO> {

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

    @Override
    public int compareTo(QuantitySizeDTO other) {
        return Integer.compare(this.size, other.size);
    }
}
