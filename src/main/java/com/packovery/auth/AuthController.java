package com.packovery.auth;

import com.packovery.auth.dto.*;
import com.packovery.common.dto.ApiResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
    public Response login(@Valid LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword(),
                    loginRequest.getFirstName(),
                    loginRequest.getLastName());

            return Response.ok()
                    .entity(ApiResponse.success("Login effettuato con successo", loginResponse))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    @POST
    @Path("/refresh")
    public Response refresh(@Valid RefreshTokenRequest refreshRequest) {
        try {
            LoginResponse loginResponse = authService.refreshToken(refreshRequest.getRefreshToken());

            return Response.ok()
                    .entity(ApiResponse.success("Token aggiornato con successo", loginResponse))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

   @POST
   @Path("/request-reset-password")
    public Response requestResetPassword(@Valid ForgotPasswordRequest request) {
        return authService.requestPasswordReset(request);
   }

   @POST
   @Path("/reset-password")
    public Response resetPassword(@Valid ResetPasswordRequest request) {
        return authService.resetPassword(request);
   }
}
