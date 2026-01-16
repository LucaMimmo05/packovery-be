package com.packovery.auth.dto;

import java.time.LocalDateTime;

public class BlockedResponse {

    private String message;
    private String email;
    private boolean permanent;
    private LocalDateTime blockedUntil;
    private Long minutesLeft;

    public BlockedResponse() {}

    public BlockedResponse(String message, String email, boolean permanent, LocalDateTime blockedUntil, Long minutesLeft) {
        this.message = message;
        this.email = email;
        this.permanent = permanent;
        this.blockedUntil = blockedUntil;
        this.minutesLeft = minutesLeft;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isPermanent() { return permanent; }
    public void setPermanent(boolean permanent) { this.permanent = permanent; }

    public LocalDateTime getBlockedUntil() { return blockedUntil; }
    public void setBlockedUntil(LocalDateTime blockedUntil) { this.blockedUntil = blockedUntil; }

    public Long getMinutesLeft() { return minutesLeft; }
    public void setMinutesLeft(Long minutesLeft) { this.minutesLeft = minutesLeft; }
}

