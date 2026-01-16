package com.packovery.auth;

import com.packovery.auth.dto.BlockedResponse;
import com.packovery.auth.dto.LoginResponse;
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

import java.time.LocalDateTime;



@ApplicationScoped
public class AuthService {

    @Inject
    AuthRepository authRepository;

    @Inject
    JwtService jwtService;

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
        return new LoginResponse(token, "Login avvenuto con successo", user.getEmail());
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
                authRepository.blockUser(user.id, 0);
            }
        }
        return null;
    }
}
