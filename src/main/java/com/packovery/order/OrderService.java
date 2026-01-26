package com.packovery.order;

import com.packovery.order.dto.*;
import com.packovery.user.User;
import com.packovery.vehicle.Vehicle;
import com.packovery.location.OrderLocation;
import com.packovery.common.enums.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import io.quarkus.panache.common.Parameters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@ApplicationScoped
public class OrderService {

    @Transactional
    public OrderDetailResponse createOrder(CreateOrderRequest request) {
        String trackingCode = "PKV" + System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        Order order = new Order(OrderStatus.PENDING, trackingCode, request.getSenderId());
        order.setPackageSize(request.getPackageSize());
        order.setPackageWeight(request.getPackageWeight());
        order.setActualWeight(request.getActualWeight());
        order.setActualSize(request.getActualSize());
        order.setCreationDate(now);
        order.setCreatedAt(now);
        order.setUpdatedAt(now);


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
            location.setCreatedAt(now);
            location.setUpdatedAt(now);
            location.persist();

            order.setLocation(location);
        }

        return toDetailResponse(order);
    }

    public List<OrderResponse> getAllOrders(Long id, OrderStatus status, String pickUpCity,
                                            String pickUpProvince, String deliveryCity, String deliveryProvince,
                                            PackageWeight weight, PackageSize size, String createdAt) {

        // "JOIN FETCH" carica la location in una sola query, evitando errori di LazyInitialization
        StringBuilder query = new StringBuilder("FROM Order o LEFT JOIN FETCH o.location loc WHERE 1=1");
        Parameters params = new Parameters();

        if (id != null) {
            query.append(" AND o.id = :id");
            params.and("id", id);
        }
        if (status != null) {
            query.append(" AND o.status = :status");
            params.and("status", status);
        }
        if (weight != null) {
            query.append(" AND o.packageWeight = :weight");
            params.and("weight", weight);
        }
        if (size != null) {
            query.append(" AND o.packageSize = :size");
            params.and("size", size);
        }
        if (pickUpCity != null && !pickUpCity.isBlank()) {
            query.append(" AND loc.pickupCity LIKE :pickUpCity");
            params.and("pickUpCity", "%" + pickUpCity + "%");
        }
        if (deliveryCity != null && !deliveryCity.isBlank()) {
            query.append(" AND loc.deliveryCity LIKE :deliveryCity");
            params.and("deliveryCity", "%" + deliveryCity + "%");
        }

        if (createdAt != null && !createdAt.isBlank()) {
            try {
                LocalDate date = LocalDate.parse(createdAt);
                query.append(" AND o.creationDate >= :start AND o.creationDate < :end");
                params.and("start", date.atStartOfDay());
                params.and("end", date.plusDays(1).atStartOfDay());
            } catch (DateTimeParseException e) {
            }
        }

        return Order.<Order>find(query.toString(), params)
                .stream()
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
        OrderLocation loc = order.getLocation();
        return new OrderResponse(
                order.id,
                order.getTrackingCode(),
                order.getStatus(),
                loc != null ? loc.getPickupCity() : null,
                loc != null ? loc.getPickupProvince() : null,
                loc != null ? loc.getDeliveryCity() : null,
                loc != null ? loc.getDeliveryProvince() : null,
                order.getPackageWeight(),
                order.getPackageSize(),
                order.getCreationDate()
        );
    }

    private OrderDetailResponse toDetailResponse(Order order) {
        User creator = (order.getSenderId() != null) ? User.findById(order.getSenderId()) : null;
        User rider = order.getRider();
        Vehicle vehicle = order.getVehicle();
        OrderLocation loc = order.getLocation();

        return new OrderDetailResponse(
                order.id,
                creator != null ? creator.getFirstName() : null,
                creator != null ? creator.getLastName() : null,
                order.getStatus(),
                order.getActualWeight(),
                order.getActualSize(),
                rider != null ? rider.getFirstName() : null,
                rider != null ? rider.getLastName() : null,
                order.getCreationDate() != null ? order.getCreationDate() : null,
                loc != null ? loc.getEstimatedArrival() : null,
                vehicle != null ? vehicle.getType() : null,
                vehicle != null ? vehicle.getLicensePlate() : null,
                loc != null ? loc.getDeliveryLatitude(): null,
                loc != null ? loc.getDeliveryLongitude() : null,
                loc != null ? loc.getPickupLatitude() : null,
                loc != null ? loc.getPickupLongitude() : null
        );
    }
}