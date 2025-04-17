package org.onlineshop.model.entity;

import jakarta.persistence.*;
import org.onlineshop.model.enums.SizeName;

@Entity
@Table(name = "sizes")
public class ShoeSize extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private SizeName sizeName;

    @Column(nullable = false, unique = true)
    private Integer size;

    public ShoeSize() {
    }

    public SizeName getSizeName() {
        return sizeName;
    }

    public void setSizeName(SizeName sizeName) {
        this.sizeName = sizeName;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
