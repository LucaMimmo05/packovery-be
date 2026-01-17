package com.packovery.auth;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class OtpService {

    private final ConcurrentHashMap<String, OtpEntry> otps = new ConcurrentHashMap<>();
    private static final SecureRandom random = new SecureRandom();

    public String generateOtp(String email) {
        return generateOtpWithType(email, "PASSWORD_RESET");
    }

    public String generateOtpWithType(String email, String type) {
        int otpNumber = random.nextInt(1_000_000);
        String otp = String.format("%06d", otpNumber);

        String hash = BcryptUtil.bcryptHash(otp);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

        String key = email + ":" + type;
        otps.put(key, new OtpEntry(hash, expiresAt, type));

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        return verifyOtpWithType(email, otp, "PASSWORD_RESET");
    }

    public boolean verifyOtpWithType(String email, String otp, String type) {
        String key = email + ":" + type;
        OtpEntry entry = otps.get(key);
        if (entry == null) return false;

        boolean valid = BcryptUtil.matches(otp, entry.hash)
                && LocalDateTime.now().isBefore(entry.expiresAt)
                && type.equals(entry.type);

        if (valid) otps.remove(key);
        return valid;
    }

    public boolean hasActiveResetRequest(String email) {
        String key = email + ":PASSWORD_RESET";
        OtpEntry entry = otps.get(key);
        return entry != null && LocalDateTime.now().isBefore(entry.expiresAt);
    }

    private static class OtpEntry {
        String hash;
        LocalDateTime expiresAt;
        String type;

        OtpEntry(String hash, LocalDateTime expiresAt, String type) {
            this.hash = hash;
            this.expiresAt = expiresAt;
            this.type = type;
        }
    }
}
