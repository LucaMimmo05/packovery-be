package com.packovery.location;

import com.packovery.location.dto.UpdatePositionRequest;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/tracking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class TrackingResource {

    @Inject
    TrackingService trackingService;

    @POST
    public Response updatePosition(UpdatePositionRequest updateRequest) {
        trackingService.updateRiderLocation(updateRequest.riderId, updateRequest.latitude, updateRequest.longitude);
        return Response.accepted().build();
    }

    @GET
    @Path("/last/{riderId}")
    public Response getLastPosition(@PathParam("riderId") Long riderId) {
        RiderLocation location = trackingService.getLastPosition(riderId);

        if (location == null) {
            return Response.status(404).entity("Nessuna posizione trovata").build();
        }
        return Response.ok(location).build();
    }

    @GET
    @Path("/history/{riderId}")
    public Response getHistory(@PathParam("riderId") Long riderId) {
        List<RiderLocation> history = trackingService.getRouteHistory(riderId);
        return Response.ok(history).build();
    }
}
