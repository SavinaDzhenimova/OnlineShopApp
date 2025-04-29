package org.onlineshop.model.enums;

public enum AddressType {

    OFFICE("Офис на Еконт"),
    PERSONAL("Личен адрес");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}