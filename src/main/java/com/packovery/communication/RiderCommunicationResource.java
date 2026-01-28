package com.packovery.communication;

import com.packovery.common.enums.ActionType;
import com.packovery.common.enums.EntityViewed;
import com.packovery.communication.dto.SendMessageRequest;
import com.packovery.logging.LoggingService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;

@Path("/communications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class RiderCommunicationResource {

    @Inject
    RiderCommunicationService commService;
    @Inject
    JsonWebToken jwt;
    @Inject
    LoggingService loggingService;

    public Long getUserId() {
        String subject = jwt.getSubject();

        if (subject == null) {
            return 0L;
        }

        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @RolesAllowed("CUSTOMER_CARE")
    @POST
    public Response sendMessage(SendMessageRequest request) {
        Long userId = getUserId();

        if (userId == 0) return Response.status(Response.Status.UNAUTHORIZED).build();

        commService.sendMessage(getUserId(), request.riderId, request.content, request.orderId);

        loggingService.logAction(
                userId,
                ActionType.SEND_MESSAGE,
                EntityViewed.RIDER,
                Map.of(
                        "riderId", request.riderId,
                        "orderId", request.orderId != null ? request.orderId : "N/A"
                )
        );

        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}/read")
    public Response markAsRead(@PathParam("id") String id) {
        commService.markAsRead(id);
        return Response.ok().build();
    }

    @GET
    @Path("/rider/{riderId}")
    public List<RiderCommunication> getByRider(@PathParam("riderId") Long riderId) {
        return commService.getMessagesForRider(riderId);
    }
}