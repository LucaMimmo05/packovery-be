package com.packovery.user.dto;

import com.packovery.common.enums.UserRole;
import com.packovery.common.enums.UserStatus;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String email;
    private UserRole role;
    private UserStatus accountStatus;
    private int failedAttempts;
    private LocalDateTime blockedUntil;

    public UserResponse() {}

    public UserResponse(Long id, String email, UserRole role, UserStatus accountStatus,
                       int failedAttempts, LocalDateTime blockedUntil) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.accountStatus = accountStatus;
        this.failedAttempts = failedAttempts;
        this.blockedUntil = blockedUntil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(UserStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(LocalDateTime blockedUntil) {
        this.blockedUntil = blockedUntil;
    }
}
