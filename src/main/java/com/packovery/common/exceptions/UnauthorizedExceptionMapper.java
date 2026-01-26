package com.packovery.common.exceptions;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    private static final Logger log = Logger.getLogger(UnauthorizedExceptionMapper.class);

    @Override
    public Response toResponse(UnauthorizedException exception) {
        log.warn("Tentativo di accesso non autorizzato: " + exception.getMessage());

        ApiError apiError = new ApiError(
                Response.Status.UNAUTHORIZED.getStatusCode(),
                "Non Autorizzato",
                exception.getMessage() != null ? exception.getMessage() : "Accesso non autorizzato");

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(apiError)
                .build();
    }
}
