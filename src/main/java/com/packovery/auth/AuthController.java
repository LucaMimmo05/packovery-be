package com.packovery.auth;

import com.packovery.auth.dto.*;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
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
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword(),
                                loginRequest.getFirstName(), loginRequest.getLastName());
    }

    @POST
    @Path("/refresh")
    public LoginResponse refresh(RefreshTokenRequest refreshRequest) {
        return authService.refreshToken(refreshRequest.getRefreshToken());
    }

   @POST
   @Path("/request-reset-password")
    public Response requestResetPassword(ForgotPasswordRequest request) {
       return authService.requestPasswordReset(request);
   }

   @POST
   @Path("/reset-password")
    public Response resetPassword(ResetPasswordRequest request) {
        return authService.resetPassword(request);
   }
}
