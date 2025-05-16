package org.onlineshop.model.enums;

public enum CategoryName {

    MEN("Мъжки обувки"),
    WOMEN("Дамски обувки"),
    CHILDREN("Детски обувки");

    private final String displayName;

    CategoryName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}