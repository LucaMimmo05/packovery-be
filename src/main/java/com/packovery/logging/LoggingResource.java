package com.packovery.logging;

import com.packovery.logging.dto.CreateUserLogRequest;
import com.packovery.logging.dto.LogoutRequest;
import com.packovery.logging.dto.SendMessageRequest;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class LoggingResource {

    private final LoggingService loggingService;

    @jakarta.inject.Inject
    public LoggingResource(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserLogs (@PathParam("userId") Long userId){
        List<UserLog> userLogs = loggingService.getLogsByUserId(userId);
        return Response.ok(userLogs).build();
    }

    @POST
    public Response createUserLog(CreateUserLogRequest logRequest){
        loggingService.logAction(
                logRequest.userId,
                logRequest.actionType,
                logRequest.entityViewed,
                logRequest.metadata
        );
        return Response.accepted().build();
    }

    @POST
    @Path("/message")
    public Response createMessage(SendMessageRequest messageRequest){
        loggingService.logMessage(
                messageRequest.senderId,
                messageRequest.riderId,
                messageRequest.content
        );
        return Response.accepted().build();
    }

    @POST
    @Path("/logout")
    public Response logout(LogoutRequest logoutRequest){
        loggingService.logLogout(logoutRequest.userId);
        return Response.ok("Logout effettuato con successo").build();
    }
}
