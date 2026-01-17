package com.packovery.auth;

import com.packovery.auth.dto.BlockedResponse;
import com.packovery.auth.dto.ForgotPasswordRequest;
import com.packovery.auth.dto.LoginResponse;
import com.packovery.auth.dto.ResetPasswordRequest;
import com.packovery.common.enums.UserStatus;
import com.packovery.common.exceptions.UserBlockedException;
import com.packovery.security.JwtService;
import com.packovery.user.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;



@ApplicationScoped
public class AuthService {

    @Inject
    AuthRepository authRepository;

    @Inject
    JwtService jwtService;

    @Inject
    OtpService otpService;

    @Inject
    EmailService emailService;

    @Transactional
    public LoginResponse login(String email, String password) {
        User user = authRepository.findByEmail(email.toLowerCase());

        if (user == null) throw new NotFoundException("Utente non trovato.");

        BlockedResponse blocked = checkBlocked(user);
        if (blocked != null) {
            throw new UserBlockedException(blocked);
        }

        if (!BcryptUtil.matches(password, user.getPasswordHash())) {
            int attempts = user.getFailedAttempts() + 1;
            authRepository.blockUser(user.id, attempts);
            throw new UnauthorizedException("Credenziali non valide.");
        }

        authRepository.blockUser(user.id, 0);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponse(token, refreshToken, "Login avvenuto con successo", user.getEmail());
    }

    private BlockedResponse checkBlocked(User user) {
        if (user.getAccountStatus() == UserStatus.PERM_BLOCKED) {
            return new BlockedResponse(
                    "Account permanente bloccato. Contatta l'amministratore.",
                    user.getEmail(),
                    true,
                    null,
                    null
            );
        }

        if (user.getAccountStatus() == UserStatus.TEMP_BLOCKED) {
            LocalDateTime blockedUntil = user.getBlockedUntil();
            if (blockedUntil != null && blockedUntil.isAfter(LocalDateTime.now())) {
                long minutesLeft = java.time.Duration.between(LocalDateTime.now(), blockedUntil).toMinutes();
                return new BlockedResponse(
                        "Account temporaneamente bloccato.",
                        user.getEmail(),
                        false,
                        blockedUntil,
                        minutesLeft
                );
            } else {
                authRepository.unblockUser(user.id);
                user.setAccountStatus(UserStatus.ACTIVE);
                user.setBlockedUntil(null);
                user.setFailedAttempts(0);
            }
        }
        return null;
    }

    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        try {
            JsonWebToken jwt = jwtService.validateRefreshToken(refreshToken);

            String tokenType = jwt.getClaim("type");
            if (!"refresh".equals(tokenType)) {
                throw new UnauthorizedException("Token non valido");
            }

            String email = jwt.getSubject();
            User user = authRepository.findByEmail(email);

            if (user == null) {
                throw new NotFoundException("Utente non trovato");
            }

            BlockedResponse blocked = checkBlocked(user);
            if (blocked != null) {
                throw new UserBlockedException(blocked);
            }

            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);

            return new LoginResponse(newAccessToken, newRefreshToken, "Token aggiornato con successo", user.getEmail());
        } catch (Exception e) {
            throw new UnauthorizedException("Refresh token non valido o scaduto");
        }
    }


    public User findUserByEmail(String email) {
        return authRepository.findByEmail(email.toLowerCase());
    }


    public Response requestPasswordReset(ForgotPasswordRequest request) {
        User user = findUserByEmail(request.getEmail());
        if (user == null) return Response.status(Response.Status.NOT_FOUND).entity("Email Non Trovata").build();

        String otp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp);

        return Response.ok().build();
    }

    @Transactional
    public Response resetPassword(ResetPasswordRequest request) {
        if (!otpService.hasActiveResetRequest(request.getEmail())) {
            return Response.status(400)
                    .entity("Non è stata effettuata alcuna richiesta di reset password per questa email o la richiesta è scaduta.")
                    .build();
        }

        if (!otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            return Response.status(400)
                    .entity("OTP non valido.")
                    .build();
        }

        User user = authRepository.findByEmail(request.getEmail().toLowerCase());
        if (user == null) {
            return Response.status(404)
                    .entity("Utente non trovato.")
                    .build();
        }

        user.setPasswordHash(BcryptUtil.bcryptHash(request.getNewPassword()));

        return Response.ok()
                .entity("Password reimpostata con successo.")
                .build();
    }
}
