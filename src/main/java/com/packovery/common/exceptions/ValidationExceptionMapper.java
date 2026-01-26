package com.packovery.common.exceptions;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger log = Logger.getLogger(ValidationExceptionMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        log.warn("Errore di validazione: " + exception.getMessage());

        String message = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Dati non validi");

        ApiError apiError = new ApiError(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Errore di Validazione",
                message);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(apiError)
                .build();
    }
}
