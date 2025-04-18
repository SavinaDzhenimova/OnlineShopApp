package org.onlineshop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promo_codes")
public class PromoCode extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 12)
    private String code;

    @Column(nullable = false, name = "discount_value")
    @Min(1)
    @Max(100)
    private BigDecimal discountValue;

    @Column(name = "valid_from")
    @FutureOrPresent
    private LocalDate validFrom;

    @Column(name = "valid_to")
    @FutureOrPresent
    private LocalDate validTo;

    @Column
    private boolean active;

    public PromoCode() {
        this.active = false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
