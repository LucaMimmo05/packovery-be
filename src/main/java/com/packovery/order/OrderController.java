package com.packovery.order;

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
                    .entity(order)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Errore nella creazione dell'ordine: " + e.getMessage()))
                    .build();
        }
    }



    @GET
    public List<OrderResponse> getAllOrders(
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
        return orderService.getAllOrders(id, status, pickUpCity, pickUpProvince, deliveryCity, deliveryProvince, weight, size, createdAt);
    }

    @GET
    @Path("/{id}")
    public OrderDetailResponse getOrderById(@PathParam("id") Long id) {
        return orderService.getOrderById(id);
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
