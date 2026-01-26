package com.packovery.location;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RiderLocationRepository implements PanacheMongoRepository<RiderLocation> {
}
