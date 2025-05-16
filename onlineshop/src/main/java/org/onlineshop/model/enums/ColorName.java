package org.onlineshop.model.enums;

public enum ColorName {

    BLACK("Черен"),
    WHITE("Бял"),
    RED("Червен"),
    BLUE("Син"),
    GREEN("Зелен"),
    GREY("Сив"),
    BROWN("Кафяв"),
    PINK("Розов"),
    YELLOW("Жълт"),
    ORANGE("Оранжев"),
    BEIGE("Бежов"),
    GOLD("Златен"),
    CORAL("Корал"),
    BORDEAUX("Бордо"),
    BRONZE("Бронзов"),
    SILVER("Сребърен"),
    CAMOUFLAGE("Камуфлаж"),
    LEOPARD("Леопардов"),
    MULTICOLOR("Многоцветен");

    private final String displayName;

    ColorName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}