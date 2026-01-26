package com.packovery.order;

import com.packovery.order.dto.OrderResponse;
import com.packovery.order.dto.OrderDetailResponse;
import com.packovery.order.dto.CreateOrderRequest;
import com.packovery.user.User;
import com.packovery.vehicle.Vehicle;
import com.packovery.location.OrderLocation;
import com.packovery.common.enums.PackageWeight;
import com.packovery.common.enums.PackageSize;
import com.packovery.common.enums.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderService {

    @Transactional
    public OrderDetailResponse createOrder(CreateOrderRequest request) {
        String trackingCode = "PKV" + System.currentTimeMillis();

        Order order = new Order(OrderStatus.PENDING, trackingCode, request.getSenderId());
        order.setPackageSize(request.getPackageSize());
        order.setPackageWeight(request.getPackageWeight());
        order.setActualWeight(request.getActualWeight());
        order.setActualSize(request.getActualSize());
        order.setCreationDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        if (request.getRiderId() != null) {
            User rider = User.findById(request.getRiderId());
            if (rider != null) {
                order.setRider(rider);
                order.setStatus(OrderStatus.ASSIGNED);
            }
        }

        if (request.getVehicleId() != null) {
            Vehicle vehicle = Vehicle.findById(request.getVehicleId());
            if (vehicle != null) {
                order.setVehicle(vehicle);
            }
        }

        order.persist();

        if (request.getPickupCity() != null || request.getDeliveryCity() != null) {
            OrderLocation location = new OrderLocation();
            location.setOrder(order);
            location.setPickupCity(request.getPickupCity());
            location.setPickupProvince(request.getPickupProvince());
            location.setDeliveryCity(request.getDeliveryCity());
            location.setDeliveryProvince(request.getDeliveryProvince());
            location.setCreatedAt(LocalDateTime.now());
            location.setUpdatedAt(LocalDateTime.now());
            location.persist();

            order.setLocation(location);
        }

        return toDetailResponse(order);
    }

    public List<OrderResponse> getAllOrders(Long id, OrderStatus status, String pickUpCity,
                                           String pickUpProvince, String deliveryCity, String deliveryProvince,
                                           PackageWeight weight, PackageSize size, String createdAt) {
        StringBuilder query = new StringBuilder("select o from Order o left join o.location loc where 1=1");
        Parameters params = new Parameters();

        if (id != null) {
            query.append(" and o.id = :id");
            params.and("id", id);
        }

        if (status != null) {
            query.append(" and o.status = :status");
            params.and("status", status);
        }

        if (weight != null) {
            query.append(" and o.packageWeight = :weight");
            params.and("weight", weight);
        }

        if (size != null) {
            query.append(" and o.packageSize = :size");
            params.and("size", size);
        }
        if (pickUpCity != null && !pickUpCity.isEmpty()) {
            query.append(" and loc.pickupCity like :pickUpCity");
            params.and("pickUpCity", "%" + pickUpCity + "%");
        }

        if (pickUpProvince != null && !pickUpProvince.isEmpty()) {
            query.append(" and loc.pickupProvince like :pickUpProvince");
            params.and("pickUpProvince", "%" + pickUpProvince + "%");
        }

        if (deliveryCity != null && !deliveryCity.isEmpty()) {
            query.append(" and loc.deliveryCity like :deliveryCity");
            params.and("deliveryCity", "%" + deliveryCity + "%");
        }

        if (deliveryProvince != null && !deliveryProvince.isEmpty()) {
            query.append(" and loc.deliveryProvince like :deliveryProvince");
            params.and("deliveryProvince", "%" + deliveryProvince + "%");
        }

        if (createdAt != null && !createdAt.isEmpty()) {
            try {
                LocalDateTime date = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                query.append(" and o.creationDate >= :createdAt and o.creationDate < :createdAtEnd");
                params.and("createdAt", date);
                params.and("createdAtEnd", date.plusDays(1));
            } catch (Exception e) {
            }
        }

        return Order.<Order>find(query.toString(), params).stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderDetailResponse getOrderById(Long id) {
        Order order = Order.findById(id);
        if (order == null) {
            throw new NotFoundException("Ordine con ID " + id + " non trovato.");
        }
        return toDetailResponse(order);
    }


    private OrderResponse toResponse(Order order) {
        String pickUpCity = null;
        String pickUpProvince = null;
        String deliveryCity = null;
        String deliveryProvince = null;

        if (order.getLocation() != null) {
            pickUpCity = order.getLocation().getPickupCity();
            pickUpProvince = order.getLocation().getPickupProvince();
            deliveryCity = order.getLocation().getDeliveryCity();
            deliveryProvince = order.getLocation().getDeliveryProvince();
        }

        return new OrderResponse(
                order.id,
                order.getStatus(),
                pickUpCity,
                pickUpProvince,
                deliveryCity,
                deliveryProvince,
                order.getPackageWeight(),
                order.getPackageSize(),
                order.getCreationDate()
        );
    }

    private OrderDetailResponse toDetailResponse(Order order) {
        User creator = null;
        if (order.getSenderId() != null) {
            creator = User.findById(order.getSenderId());
        }

        User rider = order.getRider();

        Vehicle vehicle = order.getVehicle();

        LocalDateTime estimatedArrival = null;
        if (order.getLocation() != null) {
            estimatedArrival = order.getLocation().getEstimatedArrival();
        }

        return new OrderDetailResponse(
                order.id,
                creator != null ? creator.getFirstName() : null,
                creator != null ? creator.getLastName() : null,
                order.getStatus(),
                order.getActualWeight(),
                order.getActualSize(),
                rider != null ? rider.getFirstName() : null,
                rider != null ? rider.getLastName() : null,
                estimatedArrival,
                vehicle != null ? vehicle.getType() : null,
                vehicle != null ? vehicle.getLicensePlate() : null
        );
    }
}
