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
        // Controllo se l'email esiste già
        User existingUser = User.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new WebApplicationException("Un utente con questa email esiste già", Response.Status.CONFLICT);
        }

        // Cripta la password
        String hashedPassword = BcryptUtil.bcryptHash(request.getPassword());

        // Crea il nuovo utente
        User user = new User(request.getEmail(), hashedPassword, request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.persist();

        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return User.<User>listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Utente non trovato", Response.Status.NOT_FOUND);
        }
        return toResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = User.findByEmail(email);
        if (user == null) {
            throw new WebApplicationException("Utente non trovato", Response.Status.NOT_FOUND);
        }
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Utente non trovato", Response.Status.NOT_FOUND);
        }
        user.delete();
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
