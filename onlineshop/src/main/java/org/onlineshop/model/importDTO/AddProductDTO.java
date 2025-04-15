package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class AddProductDTO {

    @NotBlank(message = "Трябва да въведете наименование на продукта!")
    @Size(min = 5, max = 50, message = "Наименованието на продукта трябва да бъде между 5 и 50 символа!")
    private String name;

    @NotBlank(message = "Трябва да добавите описание на продукта!")
    @Size(min = 5, max = 500, message = "Описанието на продукта трябва да бъде между 5 и 500 символа!")
    private String description;

    @NotBlank(message = "Трябва да посочите цена на продукта!")
    @Positive(message = "Цената на продукта не може да бъде отрицателно число или 0!")
    private BigDecimal price;

    @NotNull(message = "Изберете марка!")
    private Long brandId;

    @NotEmpty(message = "Изберете поне една категория!")
    private Set<Long> categories;

    private Set<QuantitySizeDTO> sizes;

    public AddProductDTO() {
        this.categories = new HashSet<>();
        this.sizes = new HashSet<>();
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Set<Long> getCategories() {
        return categories;
    }

    public void setCategories(Set<Long> categories) {
        this.categories = categories;
    }

    public Set<QuantitySizeDTO> getSizes() {
        return sizes;
    }

    public void setSizes(Set<QuantitySizeDTO> sizes) {
        this.sizes = sizes;
    }
}
