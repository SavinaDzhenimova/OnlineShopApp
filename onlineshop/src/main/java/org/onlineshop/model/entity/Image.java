package org.onlineshop.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {

    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public Image() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}