package com.packovery.common.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger log = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Errore imprevisto", exception);

        ApiError apiError = new ApiError(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Errore Interno del Server",
                "Si è verificato un errore imprevisto");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(apiError)
                .build();
    }

}

