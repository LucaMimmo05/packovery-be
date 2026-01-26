package com.packovery.order;

import com.packovery.common.dto.ApiResponse;
import com.packovery.order.dto.*;
import com.packovery.common.enums.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Controller per la gestione degli ordini.
 * L'accesso è limitato agli utenti con ruolo CUSTOMER_CARE.
 */
@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("CUSTOMER_CARE")
public class OrderController {

    @Inject
    OrderService orderService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(@Valid CreateOrderRequest request) {
        OrderDetailResponse order = orderService.createOrder(request);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Ordine creato con successo", order))
                .build();
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
            @QueryParam("createdAt") String createdAt) {

        List<OrderResponse> orders = orderService.getAllOrders(
                id, status, pickUpCity, pickUpProvince,
                deliveryCity, deliveryProvince, weight, size, createdAt);

        return Response.ok()
                .entity(ApiResponse.success("Ordini recuperati con successo", orders))
                .build();
    }

    @GET
    @Path("/{trackingCode}")
    public Response getOrderByTrackingCode(@PathParam("trackingCode") String trackingCode) {
        OrderDetailResponse order = orderService.getOrderByTrackingCode(trackingCode);
        return Response.ok()
                .entity(ApiResponse.success("Ordine recuperato con successo", order))
                .build();
    }
}