package com.packovery.alert;

import com.packovery.common.enums.AlertType;
import com.packovery.common.enums.IssueResolution;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;

@MongoEntity(collection = "alert_issues")
public class AlertIssue extends PanacheMongoEntity {
    public String alertId;
    public Long issueRelatedOrderId;
    public Instant issueCreationTime;
    public IssueResolution resolution = IssueResolution.OPEN;
    public Instant alertResolvedTime;
    public Long resolvedBy;
    public String resolutionDescription;
    public String snapshotAlertName;
    public AlertType snapshotAlertType;
}
