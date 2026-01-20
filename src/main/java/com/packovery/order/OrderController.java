package com.packovery.order;

import com.packovery.order.dto.OrderResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.math.BigDecimal;
import java.util.List;

@RolesAllowed("CUSTOMER_CARE")
@Path("/api/orders")
public class OrderController {

    @Inject
    OrderService orderService;



    @GET
    public List<OrderResponse> getAllOrders(
            @QueryParam("id") Long id,
            @QueryParam("status") String status,
            @QueryParam("pickUpCity") String pickUpCity,
            @QueryParam("pickUpProvince") String pickUpProvince,
            @QueryParam("deliveryCity") String deliveryCity,
            @QueryParam("deliveryProvince") String deliveryProvince,
            @QueryParam("weight") BigDecimal weight,
            @QueryParam("size") BigDecimal size,
            @QueryParam("createdAt") String createdAt
            ) {
        return orderService.getAllOrders(id, status, pickUpCity, pickUpProvince, deliveryCity, deliveryProvince, weight,size,createdAt);
    }
}
