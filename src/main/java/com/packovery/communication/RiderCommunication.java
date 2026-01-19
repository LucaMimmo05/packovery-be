package com.packovery.communication;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;

@MongoEntity(collection = "communications")
public class RiderCommunication extends PanacheMongoEntity {
    public Long senderId;
    public Long riderId;
    public String messageContent;
    public Instant messageSentTime;
    public boolean messageReadStatus;
}
