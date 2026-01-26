package com.packovery.alert;

import com.packovery.common.enums.AlertType;
import com.packovery.common.enums.IssueResolution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.bson.types.ObjectId;

@ApplicationScoped
public class AlertIssueService {

    @Inject
    AlertIssueRepository repository;

    @Transactional
    public void createIssue(Long orderId, String snapshotName, AlertType snapshotType) {
        AlertIssue issue = new AlertIssue();
        issue.alertId = UUID.randomUUID().toString();
        issue.issueRelatedOrderId = orderId;
        issue.snapshotAlertName = snapshotName;
        issue.snapshotAlertType = snapshotType;
        issue.issueCreationTime = Instant.now();
        issue.resolution = IssueResolution.OPEN;

        repository.persist(issue);
    }

    @Transactional
    public void resolveIssue(String mongoId, Long adminId, String notes) {
        AlertIssue issue = repository.findById(new ObjectId(mongoId));

        if (issue != null) {
            issue.resolution = IssueResolution.RESOLVED_MANUAL;
            issue.resolvedBy = adminId;
            issue.resolutionDescription = notes;
            issue.alertResolvedTime = Instant.now();
            repository.update(issue);
        }
    }

    public List<AlertIssue> getOpenIssues() {
        return repository.list("resolution", IssueResolution.OPEN);
    }

    public List<AlertIssue> getByOrder(Long orderId) {
        return repository.list("issueRelatedOrderId", orderId);
    }
}