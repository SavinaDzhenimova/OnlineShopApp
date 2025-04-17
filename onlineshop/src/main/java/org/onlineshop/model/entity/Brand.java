package org.onlineshop.model.entity;

import jakarta.persistence.*;
import org.onlineshop.model.enums.BrandName;

@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private BrandName brandName;

    @Column(nullable = false, name = "brand_url")
    private String brandUrl;

    public Brand() {
    }

    public BrandName getBrandName() {
        return brandName;
    }

    public void setBrandName(BrandName brandName) {
        this.brandName = brandName;
    }

    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }
}
