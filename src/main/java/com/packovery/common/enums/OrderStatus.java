package com.packovery.common.enums;

public enum OrderStatus {
    PENDING("In attesa"),
    ASSIGNED("Assegnato"),
    IN_TRANSIT("In transito"),
    DELIVERED("Consegnato"),
    CANCELLED("Annullato"),
    FAILED("Fallito"),
    RETURNED("Restituito");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == PENDING || this == ASSIGNED || this == IN_TRANSIT;
    }

    public boolean isCompleted() {
        return this == DELIVERED || this == CANCELLED || this == FAILED || this == RETURNED;
    }
}
