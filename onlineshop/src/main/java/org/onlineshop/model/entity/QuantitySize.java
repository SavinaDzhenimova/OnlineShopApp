package org.onlineshop.model.entity;

import jakarta.persistence.*;
import org.onlineshop.model.enums.Size;

@Entity
@Table(name = "quantities_sizes")
public class QuantitySize extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Size size;

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