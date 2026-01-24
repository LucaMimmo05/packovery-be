package com.packovery.location;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;

@MongoEntity(collection = "rider_location")
public class RiderLocation extends PanacheMongoEntity {
    public Long riderId;
    public Instant positionTimestamp;
    public Double latitude;
    public Double longitude;
    public Double distanceTraveled;
}
