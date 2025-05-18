package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AddDiscountDTO {

    @NotNull(message = "Трябва да посочите процент на отстъпката!")
    @Positive(message = "Отстъпката не може да бъде отрицателно число или 0!")
    @Min(1)
    @Max(100)
    private BigDecimal discountPercent;

    @NotNull(message = "Трябва да посочите дата за начало на отстъпката!")
    @FutureOrPresent(message = "Датата за начало на отстъпката трябва да бъде в настоящето или бъдещето!")
    private LocalDate validFrom;

    @NotNull(message = "Трябва да посочите дата за край на отстъпката!")
    @FutureOrPresent(message = "Датата за край на отстъпката трябва да бъде в настоящето или бъдещето!")
    private LocalDate validTo;

    @NotNull(message = "Трябва да посочите долна граница на ценовия диапазон!")
    @Positive(message = "Долната граница на ценовия диапазон не може да бъде отрицателно число или 0!")
    private BigDecimal minPrice;

    @NotNull(message = "Трябва да посочите горна граница на ценовия диапазон!")
    @Positive(message = "Горната граница на ценовия диапазон не може да бъде отрицателно число или 0!")
    private BigDecimal maxPrice;

    public AddDiscountDTO() {
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
}
