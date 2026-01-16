package com.packovery.auth;


import com.packovery.auth.dto.LoginRequest;
import com.packovery.auth.dto.LoginResponse;
import com.packovery.common.enums.UserRole;
import com.packovery.common.enums.UserStatus;
import com.packovery.user.User;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {


    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Object login(LoginRequest loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @POST
    @Path("/register")
    @Transactional
    public Response register() {
        User user = new User();
        user.setEmail("luca@gmail.com");
        user.setPasswordHash("$2a$10$PzM6alGAgGvNYXANIDryyu7hiJr0lXLTOAdovLPZOkOesBOOuDtdG");
        user.setRole(UserRole.USER);
        user.setAccountStatus(UserStatus.ACTIVE);
        user.setFailedAttempts(0);
        user.persist(); // funziona correttamente dentro @Transactional
        return Response.ok().build();
    }
}
