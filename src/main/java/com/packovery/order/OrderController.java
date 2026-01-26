package com.packovery.order;

import com.packovery.common.dto.ApiResponse;
import com.packovery.order.dto.OrderResponse;
import com.packovery.order.dto.OrderDetailResponse;
import com.packovery.order.dto.CreateOrderRequest;
import com.packovery.common.enums.PackageWeight;
import com.packovery.common.enums.PackageSize;
import com.packovery.common.enums.OrderStatus;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@RolesAllowed("CUSTOMER_CARE")
@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderService orderService;

    @POST
    public Response createOrder(@Valid CreateOrderRequest request) {
        try {
            OrderDetailResponse order = orderService.createOrder(request);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Ordine creato con successo", order))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    @GET
    public Response getAllOrders(
            @QueryParam("id") Long id,
            @QueryParam("status") OrderStatus status,
            @QueryParam("pickUpCity") String pickUpCity,
            @QueryParam("pickUpProvince") String pickUpProvince,
            @QueryParam("deliveryCity") String deliveryCity,
            @QueryParam("deliveryProvince") String deliveryProvince,
            @QueryParam("weight") PackageWeight weight,
            @QueryParam("size") PackageSize size,
            @QueryParam("createdAt") String createdAt
            ) {
        try {
            List<OrderResponse> orders = orderService.getAllOrders(
                    id, status, pickUpCity, pickUpProvince,
                    deliveryCity, deliveryProvince, weight, size, createdAt);

            return Response.ok()
                    .entity(ApiResponse.success("Ordini recuperati con successo", orders))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id) {
        try {
            OrderDetailResponse order = orderService.getOrderById(id);
            return Response.ok()
                    .entity(ApiResponse.success("Ordine recuperato con successo", order))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
