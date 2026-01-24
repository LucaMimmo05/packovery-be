package com.packovery.auth;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailService {


    // * DEV MOCK
    public void sendOtpEmail(String toEmail, String otp) {
        System.out.println("=== INVIO OTP ===");
        System.out.println("To: " + toEmail);
        System.out.println("OTP: " + otp);
        System.out.println("=================");
    }
}
