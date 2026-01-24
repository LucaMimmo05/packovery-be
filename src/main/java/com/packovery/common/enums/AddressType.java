package com.packovery.common.enums;

public enum AddressType {
    RESIDENTIAL("Residenziale"),
    COMMERCIAL("Commerciale"),
    INDUSTRIAL("Industriale"),
    OFFICE("Ufficio"),
    OTHER("Altro");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
