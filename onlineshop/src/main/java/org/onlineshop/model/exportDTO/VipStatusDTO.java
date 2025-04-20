package org.onlineshop.model.exportDTO;

import java.math.BigDecimal;

public class VipStatusDTO {

    private String nextLevel;

    private BigDecimal amountToNextLevel;

    private String message;

    public VipStatusDTO() {
    }

    public String getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(String nextLevel) {
        this.nextLevel = nextLevel;
    }

    public BigDecimal getAmountToNextLevel() {
        return amountToNextLevel;
    }

    public void setAmountToNextLevel(BigDecimal amountToNextLevel) {
        this.amountToNextLevel = amountToNextLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}