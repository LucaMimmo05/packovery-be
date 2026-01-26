package com.packovery.order.dto;

import com.packovery.common.enums.OrderStatus;
import com.packovery.common.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDetailResponse {

    private Long orderId;
    private String creatorFirstName;
    private String creatorLastName;
    private OrderStatus orderStatus;
    private BigDecimal packageWeight;
    private BigDecimal packageSize;
    private String riderFirstName;
    private String riderLastName;
    private LocalDateTime estimatedArrival;
    private VehicleType vehicleType;
    private String vehicleLicensePlate;

    public OrderDetailResponse() {}

    public OrderDetailResponse(Long orderId, String creatorFirstName, String creatorLastName,
                              OrderStatus orderStatus, BigDecimal packageWeight, BigDecimal packageSize,
                              String riderFirstName, String riderLastName, LocalDateTime estimatedArrival,
                              VehicleType vehicleType, String vehicleLicensePlate) {
        this.orderId = orderId;
        this.creatorFirstName = creatorFirstName;
        this.creatorLastName = creatorLastName;
        this.orderStatus = orderStatus;
        this.packageWeight = packageWeight;
        this.packageSize = packageSize;
        this.riderFirstName = riderFirstName;
        this.riderLastName = riderLastName;
        this.estimatedArrival = estimatedArrival;
        this.vehicleType = vehicleType;
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCreatorFirstName() {
        return creatorFirstName;
    }

    public void setCreatorFirstName(String creatorFirstName) {
        this.creatorFirstName = creatorFirstName;
    }

    public String getCreatorLastName() {
        return creatorLastName;
    }

    public void setCreatorLastName(String creatorLastName) {
        this.creatorLastName = creatorLastName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(BigDecimal packageWeight) {
        this.packageWeight = packageWeight;
    }

    public BigDecimal getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(BigDecimal packageSize) {
        this.packageSize = packageSize;
    }

    public String getRiderFirstName() {
        return riderFirstName;
    }

    public void setRiderFirstName(String riderFirstName) {
        this.riderFirstName = riderFirstName;
    }

    public String getRiderLastName() {
        return riderLastName;
    }

    public void setRiderLastName(String riderLastName) {
        this.riderLastName = riderLastName;
    }

    public LocalDateTime getEstimatedArrival() {
        return estimatedArrival;
    }

    public void setEstimatedArrival(LocalDateTime estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }
}
