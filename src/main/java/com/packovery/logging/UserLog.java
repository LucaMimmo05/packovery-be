package com.packovery.logging;
import com.packovery.common.enums.ActionType;
import com.packovery.common.enums.EntityViewed;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;
import java.util.Map;

@MongoEntity(collection = "audit_logs")
public class UserLog extends PanacheMongoEntity{
    public Long userId;
    public ActionType actionType;
    public EntityViewed entityViewed;
    public Instant eventTimestamp;
    public Instant userSessionStart;
    public Instant userSessionEnd;
    public Map<String, Object> filtersUsed;
}
