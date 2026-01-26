package com.packovery.common.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import java.time.LocalDateTime;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger log = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Errore catturato dal GlobalMapper: " + exception.getMessage(), exception);

        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String error = "Errore Interno del Server";
        String message = "Si è verificato un errore imprevisto";

        if (exception instanceof WebApplicationException webAppException) {
            status = webAppException.getResponse().getStatus();
            error = Response.Status.fromStatusCode(status).getReasonPhrase();
            message = webAppException.getMessage();
        }
        else if (exception instanceof jakarta.validation.ValidationException) {
            status = 400;
            error = "Bad Request";
            message = exception.getMessage();
        }

        ApiError apiError = new ApiError(
                status,
                error,
                message
        );


        return Response.status(status)
                .entity(apiError)
                .build();
    }
}