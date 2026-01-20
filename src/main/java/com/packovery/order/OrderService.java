package com.packovery.order;

import com.packovery.order.dto.OrderResponse;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderService {

    public List<OrderResponse> getAllOrders(Long id, String status, String pickUpCity,
                                           String pickUpProvince, String deliveryCity, String deliveryProvince,
                                           BigDecimal weight, BigDecimal size, String createdAt) {
        StringBuilder query = new StringBuilder("select o from Order o left join o.location loc where 1=1");
        Parameters params = new Parameters();

        if (id != null) {
            query.append(" and o.id = :id");
            params.and("id", id);
        }

        if (status != null && !status.isEmpty()) {
            query.append(" and o.status = :status");
            params.and("status", status);
        }

        if (weight != null) {
            query.append(" and o.actualWeight = :weight");
            params.and("weight", weight);
        }

        if (size != null) {
            query.append(" and o.actualSize = :size");
            params.and("size", size);
        }
        // Filtri separati per pickup e delivery
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
                // Ignora se il formato della data non è valido
            }
        }

        return Order.<Order>find(query.toString(), params).stream()
                .map(this::toResponse)
                .toList();
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
                order.getActualWeight(),
                order.getActualSize(),
                order.getCreationDate()
        );
    }
}
