package org.onlineshop.model.entity;

import jakarta.persistence.*;
import org.onlineshop.model.enums.ColorName;

@Entity
@Table(name = "colors")
public class Color extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ColorName colorName;

    @Column(nullable = false)
    private String description;

    public Color() {
    }

    public ColorName getColorName() {
        return colorName;
    }

    public void setColorName(ColorName colorName) {
        this.colorName = colorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
