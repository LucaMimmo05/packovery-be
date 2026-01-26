package com.packovery.auth.dto;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String message;
    private String email;

    public LoginResponse(String accessToken, String refreshToken, String message, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
