package com.packovery.order;

import com.packovery.common.enums.OrderStatus;
import com.packovery.common.enums.PackageSize;
import com.packovery.common.enums.PackageWeight;
import com.packovery.location.OrderLocation;
import com.packovery.user.User;
import com.packovery.vehicle.Vehicle;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private User rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private OrderStatus status;

    @Column(name = "tracking_code", unique = true, length = 50)
    private String trackingCode;

    @Column(name = "actual_size", precision = 10, scale = 2)
    @PositiveOrZero
    private BigDecimal actualSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_size", nullable = false)
    @NotNull
    private PackageSize packageSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_weight", nullable = false)
    @NotNull
    private PackageWeight packageWeight;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "actual_weight", precision = 10, scale = 3)
    @PositiveOrZero
    private BigDecimal actualWeight;

    @Column(name = "overweight", columnDefinition = "boolean default false")
    private boolean overweight;

    @Column(name = "oversize", columnDefinition = "boolean default false")
    private boolean oversize;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private OrderLocation location;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Order() {}

    public Order(OrderStatus status, String trackingCode, Long senderId) {
        this.status = status;
        this.trackingCode = trackingCode;
        this.senderId = senderId;
    }

    public static List<Order> getAllOrders() {
        return listAll();
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public BigDecimal getActualSize() {
        return actualSize;
    }

    public void setActualSize(BigDecimal actualSize) {
        this.actualSize = actualSize;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    public boolean getOverweight() {
        return overweight;
    }

    public void setOverweight(boolean overweight) {
        this.overweight = overweight;
    }

    public boolean getOversize() {
        return oversize;
    }

    public void setOversize(boolean oversize) {
        this.oversize = oversize;
    }

    public OrderLocation getLocation() {
        return location;
    }

    public void setLocation(OrderLocation location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
