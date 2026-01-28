package com.packovery.user;

import com.packovery.common.dto.ApiResponse;
import com.packovery.common.enums.ActionType;
import com.packovery.common.enums.EntityViewed;
import com.packovery.logging.LoggingService;
import com.packovery.user.dto.CreateUserRequest;
import com.packovery.user.dto.UserResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Map;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @Inject
    LoggingService loggingService;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed("CUSTOMER_CARE")
    public Response createUser(@Valid CreateUserRequest request) {
        try {
            UserResponse user = userService.createUser(request);

            loggingService.logAction(
                    getUserId(),
                    ActionType.CREATE,
                    EntityViewed.APPLICATION, // <--- CAMBIATO QUI (era USER)
                    Map.of(
                            "createdUserEmail", user.getEmail(),
                            "createdUserRole", user.getRole()
                    )
            );

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Utente creato con successo", user))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    private Long getUserId() {
        String subject = jwt.getSubject();
        if (subject == null) return 0L;
        try { return Long.valueOf(subject); } catch (NumberFormatException e) { return 0L; }
    }
}
