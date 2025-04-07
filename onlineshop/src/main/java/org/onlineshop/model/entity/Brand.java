package org.onlineshop.model.entity;

import jakarta.persistence.*;
import org.onlineshop.model.enums.BrandName;

@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private BrandName brandName;

    public Brand() {
    }

    public BrandName getBrandName() {
        return brandName;
    }

    public void setBrandName(BrandName brandName) {
        this.brandName = brandName;
    }
}
