package com.packovery.communication;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RiderCommunicationRepository implements PanacheMongoRepository<RiderCommunication> {
}
