package com.packovery.location;

import com.packovery.common.enums.ActionType;
import com.packovery.common.enums.EntityViewed;
import com.packovery.location.dto.UpdatePositionRequest;
import com.packovery.logging.LoggingService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;

@Path("/api/tracking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class TrackingResource {

    @Inject
    TrackingService trackingService;
    @Inject
    LoggingService loggingService;
    @Inject
    JsonWebToken jwt;

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

        loggingService.logAction(
                getUserId(),
                ActionType.VIEW,
                EntityViewed.MAP,
                Map.of("action", "LIVE_POSITION", "targetRiderId", riderId)
        );

        return Response.ok(location).build();
    }

    @GET
    @Path("/history/{riderId}")
    public Response getHistory(@PathParam("riderId") Long riderId) {
        List<RiderLocation> history = trackingService.getRouteHistory(riderId);

        loggingService.logAction(
                getUserId(),
                ActionType.VIEW,
                EntityViewed.MAP,
                Map.of("action", "HISTORY_ROUTE", "targetRiderId", riderId)
        );

        return Response.ok(history).build();
    }

    private Long getUserId() {
        String subject = jwt.getSubject();
        if (subject == null) return 0L;
        try { return Long.valueOf(subject); } catch (NumberFormatException e) { return 0L; }
    }
}
