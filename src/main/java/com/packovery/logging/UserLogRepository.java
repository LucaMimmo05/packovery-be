package com.packovery.logging;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserLogRepository implements PanacheMongoRepository<UserLog> {
}
