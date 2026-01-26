package com.packovery.alert;

import com.packovery.alert.dto.CreateIssueRequest;
import com.packovery.alert.dto.ResolveIssueRequest;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/alert-issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class AlertIssueResource {

    @Inject
    AlertIssueService issueService;
    @Inject
    JsonWebToken jwt;

    private Long getAdminIdFromToken() {
        Object userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            return 0L;
        }
        return Long.valueOf(userIdClaim.toString());
    }

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
        issueService.createIssue(request.ruleId, request.orderId, request.alertName, request.type);
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}/resolve")
    public Response resolve(@PathParam("id") String id, ResolveIssueRequest request) {
        Long adminId = getAdminIdFromToken();

        issueService.resolveIssue(id, adminId, request.notes);
        return Response.ok().build();
    }
}