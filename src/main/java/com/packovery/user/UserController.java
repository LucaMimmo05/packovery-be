package com.packovery.user;

import com.packovery.common.dto.ApiResponse;
import com.packovery.user.dto.CreateUserRequest;
import com.packovery.user.dto.UserResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @POST
    @RolesAllowed("CUSTOMER_CARE")
    public Response createUser(@Valid CreateUserRequest request) {
        try {
            UserResponse user = userService.createUser(request);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Utente creato con successo", user))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
