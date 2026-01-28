package com.packovery.alert;

import com.packovery.alert.dto.CreateIssueRequest;
import com.packovery.alert.dto.ResolveIssueRequest;
import com.packovery.common.enums.ActionType;
import com.packovery.common.enums.EntityViewed;
import com.packovery.logging.LoggingService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Map;

@Path("/alert-issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class AlertIssueResource {

    @Inject
    AlertIssueService issueService;
    @Inject
    JsonWebToken jwt;
    @Inject
    LoggingService loggingService;

    private Long getAdminIdFromToken() {
        String subject = jwt.getSubject();
        if (subject == null) return 0L;
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            return 0L;
        }
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

        loggingService.logAction(
                adminId,
                ActionType.RESOLVE_ISSUE,
                EntityViewed.ALERT,
                Map.of(
                        "issueId", id,
                        "notes", request.notes != null ? request.notes : ""
                )
        );

        return Response.ok().build();
    }
}