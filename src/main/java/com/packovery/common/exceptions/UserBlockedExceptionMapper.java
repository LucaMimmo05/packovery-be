package com.packovery.common.exceptions;

import com.packovery.auth.dto.BlockedResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserBlockedExceptionMapper implements ExceptionMapper<UserBlockedException> {

    @Override
    public Response toResponse(UserBlockedException exception) {
        BlockedResponse response = exception.getBlockedResponse();
        return Response.status(Response.Status.FORBIDDEN)
                .entity(response)
                .build();
    }
}
