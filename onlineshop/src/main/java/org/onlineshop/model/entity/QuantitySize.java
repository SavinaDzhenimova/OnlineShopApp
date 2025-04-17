package org.onlineshop.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quantities_sizes")
public class QuantitySize extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "size_label", referencedColumnName = "size")
    private ShoeSize size;

    @Column(nullable = false)
    private int quantity;

    public QuantitySize() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShoeSize getSize() {
        return size;
    }

    public void setSize(ShoeSize size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}