package com.packovery.common.exceptions;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    private static final Logger log = Logger.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(NotFoundException exception) {
        log.warn("Risorsa non trovata: " + exception.getMessage());

        ApiError apiError = new ApiError(
                Response.Status.NOT_FOUND.getStatusCode(),
                "Non Trovato",
                exception.getMessage() != null ? exception.getMessage() : "Risorsa non trovata");

        return Response.status(Response.Status.NOT_FOUND)
                .entity(apiError)
                .build();
    }
}
