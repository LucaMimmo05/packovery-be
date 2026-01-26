package com.packovery.logging;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserLogRepository implements PanacheMongoRepository<UserLog> {
    public List<UserLog> findByUserId(Long userId) {
        return list("userId", userId);
    }
}
