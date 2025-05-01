package org.onlineshop.model.enums;

public enum DiscountCardName {

    VIP_300("ВИП 300"),
    VIP_700("ВИП 700"),
    VIP_1200("ВИП 1200");

    private final String displayName;

    DiscountCardName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
