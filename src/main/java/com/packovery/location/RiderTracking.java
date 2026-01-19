package com.packovery.location;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;

@MongoEntity(collection = "rider_tracking")
public class RiderTracking extends PanacheMongoEntity {
    public Long riderId;
    public Instant positionTimestamp;
    public Double latitude;
    public Double longitude;
    public Double distanceTraveled;
}
