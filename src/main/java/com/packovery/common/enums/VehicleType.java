package com.packovery.common.enums;

public enum VehicleType {
    BIKE("Bicicletta"),
    MOTORBIKE("Motocicletta"),
    SCOOTER("Scooter"),
    CAR("Auto"),
    VAN("Furgone");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
