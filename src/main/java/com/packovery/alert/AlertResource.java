package com.packovery.alert;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/alerts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertResource {

    @Inject
    AlertService alertService;

    @GET
    @Path("/order/{orderId}")
    public Response getAlertsByOrder(@PathParam("orderId") Long orderId) {
        List<AlertIssue> alerts = alertService.getAlertsByOrder(orderId);
        return Response.ok(alerts).build();
    }

    @GET
    @Path("/open")
    public Response getOpenAlerts() {
        return Response.ok(alertService.getOpenAlerts()).build();
    }

    @POST
    public Response createAlert(CreateAlertRequest request){
        alertService.createAlert(request.orderId, request.alertName, request.type);
        return Response.status(201).entity("Alert creato correttamente").build();
    }

    @PUT
    @Path("/{alertId}/resolve")
    public Response resolveAlert(@PathParam("alertId") String alertId, ResolveAlertRequest request){
        alertService.resolveIssue(alertId, request.adminId, request.notes, false);
        return Response.ok("Alert Risolto").build();
    }
}
