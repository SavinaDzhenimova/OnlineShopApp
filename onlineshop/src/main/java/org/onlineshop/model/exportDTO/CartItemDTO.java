package org.onlineshop.model.exportDTO;

import org.onlineshop.model.importDTO.QuantitySizeDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class CartItemDTO {

    private Long id;

    private String imageUrl;

    private String name;

    private Set<String> categories;

    private Set<QuantitySizeDTO> quantitySizes;

    private Integer selectedQuantity;

    private Integer selectedSize;

    private BigDecimal price;

    private Long tempId;

    public CartItemDTO() {
        this.quantitySizes = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public Integer getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(Integer selectedSize) {
        this.selectedSize = selectedSize;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<QuantitySizeDTO> getQuantitySizes() {
        return quantitySizes;
    }

    public void setQuantitySizes(Set<QuantitySizeDTO> quantitySizes) {
        this.quantitySizes = quantitySizes;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }
}
