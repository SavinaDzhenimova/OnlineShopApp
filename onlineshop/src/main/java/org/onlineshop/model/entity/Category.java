package org.onlineshop.model.entity;

import jakarta.persistence.*;
import org.onlineshop.model.enums.CategoryName;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    public Category() {
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
