package org.onlineshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false)
    @Size(min = 5, max = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 5, max = 500)
    private String description;

    @Column(nullable = false)
    @Positive
    private BigDecimal price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<QuantitySize> quantitySize;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories;

    public Product() {
        this.quantitySize = new HashSet<>();
        this.categories = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<QuantitySize> getQuantitySize() {
        return quantitySize;
    }

    public void setQuantitySize(Set<QuantitySize> quantitySize) {
        this.quantitySize = quantitySize;
    }
}
