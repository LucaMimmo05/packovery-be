package com.packovery.communication;

import com.packovery.communication.dto.SendMessageRequest;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/communications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class RiderCommunicationResource {

    @Inject
    RiderCommunicationService commService;
    @Inject
    JsonWebToken jwt;

    public Long getUserId() {
        Object userIdClaim = jwt.getClaim("userId");

        if (userIdClaim == null) {
            return 0L;
        }

        return Long.valueOf(userIdClaim.toString());
    }

    @POST
    public Response sendMessage(SendMessageRequest request) {
        commService.sendMessage(getUserId(), request.riderId, request.content);
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