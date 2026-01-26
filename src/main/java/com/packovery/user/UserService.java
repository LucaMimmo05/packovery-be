package com.packovery.user;

import com.packovery.user.dto.CreateUserRequest;
import com.packovery.user.dto.UserResponse;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class UserService {

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User existingUser = User.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new WebApplicationException("Un utente con questa email esiste già", Response.Status.CONFLICT);
        }

        String hashedPassword = BcryptUtil.bcryptHash(request.getPassword());

        User user = new User(request.getEmail(), hashedPassword, request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.persist();

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.id,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getAccountStatus(),
                user.getFailedAttempts(),
                user.getBlockedUntil()
        );
    }
}
