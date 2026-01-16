package com.packovery.security;

import com.packovery.user.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    public String generateToken(User user) {
        return Jwt.issuer("packovery")
                .subject(user.getEmail())
                .groups(Set.of(user.getRole().name()))
                .expiresIn(Duration.ofHours(2))
                .sign();
    }
}
