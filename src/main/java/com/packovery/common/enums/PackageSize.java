package com.packovery.common.enums;

import java.math.BigDecimal;

public enum PackageSize {
    S("S", new BigDecimal("1"), new BigDecimal("15")),
    M("M", new BigDecimal("16"), new BigDecimal("30")),
    L("L", new BigDecimal("31"), new BigDecimal("45")),
    XL("XL", new BigDecimal("46"), new BigDecimal("100"));

    private final String displayName;
    private final BigDecimal minSize;
    private final BigDecimal maxSize;

    PackageSize(String displayName, BigDecimal minSize, BigDecimal maxSize) {
        this.displayName = displayName;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getMinSize() {
        return minSize;
    }

    public BigDecimal getMaxSize() {
        return maxSize;
    }

    public String getDescription() {
        return displayName + ": " + minSize + "cm – " + maxSize + "cm";
    }

    public static PackageSize fromSize(BigDecimal size) {
        if (size == null) return null;

        for (PackageSize packageSize : values()) {
            if (size.compareTo(packageSize.minSize) >= 0 &&
                size.compareTo(packageSize.maxSize) <= 0) {
                return packageSize;
            }
        }
        return null;
    }

    public boolean isValidSize(BigDecimal size) {
        if (size == null) return false;
        return size.compareTo(minSize) >= 0 && size.compareTo(maxSize) <= 0;
    }
}
