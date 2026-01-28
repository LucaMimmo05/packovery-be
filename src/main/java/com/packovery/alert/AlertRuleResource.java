package com.packovery.alert;

import com.packovery.alert.dto.CreateRuleRequest;
import com.packovery.alert.dto.StatusRequest;
import com.packovery.alert.dto.UpdateRuleRequest;
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

@Path("/alert-rules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class AlertRuleResource {

    @Inject
    AlertRuleService ruleService;

    @Inject
    LoggingService loggingService;

    @Inject
    JsonWebToken jwt;

    public Long getUserId() {
        Object userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            String sub = jwt.getSubject();
            return (sub != null) ? Long.parseLong(sub) : 0L;
        }
        return Long.valueOf(userIdClaim.toString());
    }

    @GET
    public Response getAll() {
        return Response.ok(ruleService.getAllRules()).build();
    }

    @POST
    public Response create(CreateRuleRequest request) {
        ruleService.createRule(request.name, request.description, request.type, request.threshold);

        loggingService.logAction(
                getUserId(),
                ActionType.CREATE,
                EntityViewed.ALERT,
                Map.of("ruleName", request.name, "threshold", request.threshold)
        );

        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") String id, StatusRequest request) {
        ruleService.toggleStatus(id, request.status);

        loggingService.logAction(
                getUserId(),
                ActionType.UPDATE,
                EntityViewed.ALERT,
                Map.of("ruleId", id, "newStatus", request.status.name())
        );

        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, UpdateRuleRequest request) {
        boolean updated = ruleService.updateRule(
                id,
                request.name,
                request.description,
                request.type,
                request.threshold
        );

        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        loggingService.logAction(
                getUserId(),
                ActionType.UPDATE,
                EntityViewed.ALERT,
                Map.of(
                        "ruleId", id,
                        "updatedName", request.name,
                        "updatedThreshold", request.threshold
                )
        );

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        ruleService.deleteRule(id);

        loggingService.logAction(
                getUserId(),
                ActionType.DELETE,
                EntityViewed.ALERT,
                Map.of("ruleId", id)
        );

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getSingle(@PathParam("id") String id) {
        AlertRule rule = ruleService.getRule(id);

        if (rule == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(rule).build();
    }
}