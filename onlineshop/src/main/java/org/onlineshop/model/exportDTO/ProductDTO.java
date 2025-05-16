package org.onlineshop.model.exportDTO;

import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.importDTO.QuantitySizeDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private BrandName brand;

    private String brandUrl;

    private String color;

    private Set<QuantitySizeDTO> sizes;

    private String category;

    private List<String> imageUrls;

    private boolean isNew;

    private boolean isOnSale;

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

    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<QuantitySizeDTO> getSizes() {
        return sizes;
    }

    public void setSizes(Set<QuantitySizeDTO> sizes) {
        this.sizes = sizes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }
}