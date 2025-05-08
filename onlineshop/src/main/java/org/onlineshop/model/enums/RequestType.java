package org.onlineshop.model.enums;

public enum RequestType {

    RETURN("Връщане"),
    REPLACE("Замяна");

    private final String displayName;

    RequestType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}