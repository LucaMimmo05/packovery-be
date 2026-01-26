package com.packovery.common.enums;

import java.math.BigDecimal;

public enum PackageWeight {
    S("S", new BigDecimal("0.001"), new BigDecimal("1.000")),
    M("M", new BigDecimal("1.000"), new BigDecimal("3.000")),
    L("L", new BigDecimal("3.000"), new BigDecimal("5.000")),
    XL("XL", new BigDecimal("5.000"), new BigDecimal("10.000"));

    private final String displayName;
    private final BigDecimal minWeight;
    private final BigDecimal maxWeight;

    PackageWeight(String displayName, BigDecimal minWeight, BigDecimal maxWeight) {
        this.displayName = displayName;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getMinWeight() {
        return minWeight;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public String getDescription() {
        String minDesc = minWeight.compareTo(BigDecimal.ONE) < 0 ?
            minWeight.multiply(new BigDecimal("1000")).intValue() + "g" :
            minWeight.intValue() + "kg";
        String maxDesc = maxWeight.compareTo(BigDecimal.ONE) < 0 ?
            maxWeight.multiply(new BigDecimal("1000")).intValue() + "g" :
            maxWeight.intValue() + "kg";
        return displayName + ": " + minDesc + " – " + maxDesc;
    }

    public static PackageWeight fromWeight(BigDecimal weight) {
        if (weight == null) return null;

        for (PackageWeight packageWeight : values()) {
            if (weight.compareTo(packageWeight.minWeight) >= 0 &&
                weight.compareTo(packageWeight.maxWeight) <= 0) {
                return packageWeight;
            }
        }
        return null;
    }

    public boolean isValidWeight(BigDecimal weight) {
        if (weight == null) return false;
        return weight.compareTo(minWeight) >= 0 && weight.compareTo(maxWeight) <= 0;
    }
}
