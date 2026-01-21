package com.packovery.alert;

import com.packovery.common.enums.AlertType;
import com.packovery.common.enums.IssueResolution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AlertService {

    @Inject
    AlertIssueRepository issueRepository;

    public void createAlert(Long orderId, String alertName, AlertType type) {
        AlertIssue issue = new AlertIssue();
        issue.alertId = UUID.randomUUID().toString();
        issue.issueRelatedOrderId = orderId;
        issue.snapshotAlertName = alertName;
        issue.snapshotAlertType = type;
        issue.issueCreationTime = Instant.now();
        issue.resolution = IssueResolution.OPEN;

        issueRepository.persist(issue);
    }

    public void resolveIssue(String alertId, Long adminId, String notes, boolean isAuto) {
        AlertIssue issue = issueRepository.find("alertId = ?1", alertId).firstResult();

        if(issue != null){
            issue.resolution = isAuto ? IssueResolution.RESOLVED_AUTO : IssueResolution.RESOLVED_MANUAL;
            issue.alertResolvedTime = Instant.now();
            issue.resolvedBy = adminId;
            issue.resolutionDescription = notes;

            issueRepository.update(issue);
        }
    }

    public List<AlertIssue> getAlertsByOrder(Long orderId) {
        return issueRepository.list("issueRelatedOrderId = ?1", orderId);

    }

    public List<AlertIssue> getOpenAlerts(){
        return issueRepository.list("resolution = ?1", IssueResolution.OPEN);
    }
}
