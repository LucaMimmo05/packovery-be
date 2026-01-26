package com.packovery.order.dto;

import com.packovery.common.enums.OrderStatus;
import com.packovery.common.enums.PackageSize;
import com.packovery.common.enums.PackageWeight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private String trackingCode;
    private OrderStatus status;
    private String pickUpCity;
    private String pickUpProvince;
    private String deliveryCity;
    private String deliveryProvince;
    private PackageWeight weight;
    private PackageSize size;
    private LocalDateTime creationDate;

    public OrderResponse(Long id,String trackingCode, OrderStatus status, String pickUpCity, String pickUpProvince, String deliveryCity, String deliveryProvince, PackageWeight weight, PackageSize size, LocalDateTime creationDate) {
        this.id = id;
        this.trackingCode = trackingCode;
        this.status = status;
        this.pickUpCity = pickUpCity;
        this.pickUpProvince = pickUpProvince;
        this.deliveryCity = deliveryCity;
        this.deliveryProvince = deliveryProvince;
        this.weight = weight;
        this.size = size;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPickUpCity() {
        return pickUpCity;
    }

    public void setPickUpCity(String pickUpCity) {
        this.pickUpCity = pickUpCity;
    }

    public String getPickUpProvince() {
        return pickUpProvince;
    }

    public void setPickUpProvince(String pickUpProvince) {
        this.pickUpProvince = pickUpProvince;
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

    public PackageWeight getWeight() {
        return weight;
    }

    public void setWeight(PackageWeight weight) {
        this.weight = weight;
    }

    public PackageSize getSize() {
        return size;
    }

    public void setSize(PackageSize size) {
        this.size = size;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
