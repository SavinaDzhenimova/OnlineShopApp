package org.onlineshop.model.enums;

public enum BrandName {

    ADIDAS("Adidas"),
    ASICS("Asics"),
    CALVIN_KLEIN("Calvin Klein"),
    CATERPILLAR("Caterpillar"),
    CHAMPION("Champion"),
    COLUMBIA("Columbia"),
    CONVERSE("Converse"),
    CROCKS("Crocks"),
    DIADORA("Diadora"),
    FILA("FILA"),
    GUESS("Guess"),
    KAPPA("Kappa"),
    LACOSTE("Lacoste"),
    LOTTO("Lotto"),
    NAPAPIJRI("Napapijri"),
    NEW_BALANCE("New Balance"),
    NIKE("Nike"),
    PALLADIUM("Palladium"),
    PUMA("Puma"),
    REEBOK("Reebok"),
    REEF("Reef"),
    RIP_CURL("Rip Curl"),
    SALOMON("Salomon"),
    SKECHERS("Skechers"),
    THE_NORTH_FACE("The North Face"),
    TIMBERLAND("Timberland"),
    TOMMY_HILFIGER("Tommy Hilfiger"),
    UNDER_ARMOR("Under Armor"),
    US_POLO_ASSN("US Polo Assn");

    private final String displayName;

    BrandName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}