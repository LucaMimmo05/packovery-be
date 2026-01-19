package com.packovery.alert;

import com.packovery.common.enums.AlertStatus;
import com.packovery.common.enums.AlertType;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "alert_rules")
public class AlertRule extends PanacheMongoEntity {
    public String name;
    public String description;
    public AlertStatus status;
    public AlertType type;
    public String customTypeText;
    public String timeThreshold; //soglia
}
