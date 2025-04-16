package org.onlineshop.model.exportDTO;

import org.onlineshop.model.entity.Image;
import org.onlineshop.model.enums.BrandName;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private BrandName brand;

    private Set<Integer> sizes;

    private Set<String> categories;

    private List<String> imageUrls;

    public ProductDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BrandName getBrand() {
        return brand;
    }

    public void setBrand(BrandName brand) {
        this.brand = brand;
    }

    public Set<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Set<Integer> sizes) {
        this.sizes = sizes;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}