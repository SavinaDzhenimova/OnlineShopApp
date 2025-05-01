package org.onlineshop.model.exportDTO;

import java.math.BigDecimal;

public class DiscountCardDTO {

    private String discountCardName;

    private BigDecimal discountPercent;

    private BigDecimal discountValue;

    public DiscountCardDTO() {
    }

    public String getDiscountCardName() {
        return discountCardName;
    }

    public void setDiscountCardName(String discountCardName) {
        this.discountCardName = discountCardName;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
}
