package com.packovery.auth.dto;

import jakarta.validation.constraints.NotNull;

public class NewPasswordRequest {

    @NotNull
    private String password;

    @NotNull
    private String email;


    public NewPasswordRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public NewPasswordRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
