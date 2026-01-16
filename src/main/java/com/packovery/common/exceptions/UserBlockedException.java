package com.packovery.common.exceptions;


import com.packovery.auth.dto.BlockedResponse;

public class UserBlockedException extends RuntimeException {

    private final BlockedResponse blockedResponse;

    public UserBlockedException(BlockedResponse blockedResponse) {
        this.blockedResponse = blockedResponse;
    }

    public BlockedResponse getBlockedResponse() {
        return blockedResponse;
    }
}

