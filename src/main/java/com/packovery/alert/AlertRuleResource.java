package com.packovery.alert;

import com.packovery.alert.dto.CreateRuleRequest;
import com.packovery.alert.dto.StatusRequest;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/alert-rules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class AlertRuleResource {

    @Inject
    AlertRuleService ruleService;

    @GET
    public Response getAll() {
        return Response.ok(ruleService.getAllRules()).build();
    }

    @POST
    public Response create(CreateRuleRequest request) {
        ruleService.createRule(request.name, request.description, request.type, request.threshold);

        // TODO: Scommentare e ottenere l'id dell'user loggato tramite jwt
        //loggingService.logAlertCreation(currentUserId, request.name);
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") String id, StatusRequest request) {
        ruleService.toggleStatus(id, request.status);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        ruleService.deleteRule(id);
        return Response.noContent().build();
    }
}