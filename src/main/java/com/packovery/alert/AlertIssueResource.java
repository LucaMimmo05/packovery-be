package com.packovery.alert;

import com.packovery.alert.dto.CreateIssueRequest;
import com.packovery.alert.dto.ResolveIssueRequest;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/alert-issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class AlertIssueResource {

    @Inject
    AlertIssueService issueService;

    @GET
    @Path("/open")
    public Response getOpen() {
        return Response.ok(issueService.getOpenIssues()).build();
    }

    @GET
    @Path("/order/{orderId}")
    public Response getByOrder(@PathParam("orderId") Long orderId) {
        return Response.ok(issueService.getByOrder(orderId)).build();
    }

    @POST
    public Response createManualIssue(CreateIssueRequest request) {
        issueService.createIssue(request.orderId, request.alertName, request.type);
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}/resolve")
    public Response resolve(@PathParam("id") String id, ResolveIssueRequest request) {
        issueService.resolveIssue(id, request.adminId, request.notes);
        return Response.ok().build();
    }
}