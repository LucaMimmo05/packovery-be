package com.packovery.auth.dto;

public class LoginResponse {
    private String token;
    private String refreshToken;
    private String message;
    private String email;

    public LoginResponse(String token, String refreshToken, String message, String email) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.message = message;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
