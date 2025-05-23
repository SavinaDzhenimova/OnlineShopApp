package org.onlineshop.model.exportDTO;

import org.onlineshop.model.importDTO.QuantitySizeDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class CartItemDTO {

    private Long id;

    private String imageUrl;

    private String name;

    private String category;

    private Set<QuantitySizeDTO> quantitySizes;

    private Integer selectedQuantity;

    private Integer selectedSize;

    private BigDecimal unitPrice;

    private BigDecimal quantityPrice;

    private Long productId;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantityPrice() {
        return quantityPrice;
    }

    public void setQuantityPrice(BigDecimal quantityPrice) {
        this.quantityPrice = quantityPrice;
    }

    public Set<QuantitySizeDTO> getQuantitySizes() {
        return quantitySizes;
    }

    public void setQuantitySizes(Set<QuantitySizeDTO> quantitySizes) {
        this.quantitySizes = quantitySizes;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }
}
