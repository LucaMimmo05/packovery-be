package com.packovery.common.enums;

public enum AlertType {
    DELAY_START("RITARDO PARTENZA"),
    DELAY_DELIVERY("RITARDO CONSEGNA"),
    GPS_LOST("SEGNALE GPS PERSO");

    private final String description;

    AlertType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}