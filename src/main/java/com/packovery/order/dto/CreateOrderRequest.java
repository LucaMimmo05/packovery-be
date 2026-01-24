package com.packovery.order.dto;

import com.packovery.common.enums.OrderStatus;
import com.packovery.common.enums.PackageSize;
import com.packovery.common.enums.PackageWeight;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class CreateOrderRequest {

    @NotNull(message = "Il sender ID è obbligatorio")
    private Long senderId;

    private Long riderId;

    private Long vehicleId;

    @NotNull(message = "Il package size è obbligatorio")
    private PackageSize packageSize;

    @NotNull(message = "Il package weight è obbligatorio")
    private PackageWeight packageWeight;

    @PositiveOrZero
    private BigDecimal actualWeight;

    @PositiveOrZero
    private BigDecimal actualSize;

    private String pickupCity;
    private String pickupProvince;
    private String deliveryCity;
    private String deliveryProvince;

    public CreateOrderRequest() {}

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public PackageSize getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(PackageSize packageSize) {
        this.packageSize = packageSize;
    }

    public PackageWeight getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(PackageWeight packageWeight) {
        this.packageWeight = packageWeight;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    public BigDecimal getActualSize() {
        return actualSize;
    }

    public void setActualSize(BigDecimal actualSize) {
        this.actualSize = actualSize;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getPickupProvince() {
        return pickupProvince;
    }

    public void setPickupProvince(String pickupProvince) {
        this.pickupProvince = pickupProvince;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryProvince() {
        return deliveryProvince;
    }

    public void setDeliveryProvince(String deliveryProvince) {
        this.deliveryProvince = deliveryProvince;
    }
}
