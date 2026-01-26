package com.packovery.security;

import com.packovery.user.User;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.auth.principal.JWTParser;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    @Inject
    JWTParser jwtParser;

    public String generateToken(User user) {
        return Jwt.issuer("packovery")
                .subject(String.valueOf(user.id))
                .groups(Set.of(user.getRole().name()))
                .expiresIn(Duration.ofHours(2))
                .sign();
    }

    public String generateRefreshToken(User user) {
        return Jwt.issuer("packovery")
                .subject(user.getEmail())
                .claim("type", "refresh")
                .groups(Set.of(user.getRole().name()))
                .expiresIn(Duration.ofDays(30))
                .sign();
    }

    public JsonWebToken validateRefreshToken(String refreshToken) {
        try {
            return jwtParser.parse(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Token non valido", e);
        }
    }
}
