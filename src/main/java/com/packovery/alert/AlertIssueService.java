package com.packovery.alert;

import com.packovery.common.enums.IssueResolution;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.bson.types.ObjectId;

@ApplicationScoped
public class AlertIssueService {

    @Inject
    AlertIssueRepository repository;

    @Inject
    AlertRuleRepository ruleRepository;

    @Transactional
    public void createIssue(String ruleId, Long orderId) {
        if (existsOpenIssue(ruleId, orderId)) return;

        AlertRule rule = ruleRepository.findById(new ObjectId(ruleId));
        if (rule == null) return;

        AlertIssue issue = new AlertIssue();
        issue.alertId = ruleId;
        issue.issueRelatedOrderId = orderId;

        issue.snapshotAlertName = rule.type.getDescription();
        issue.snapshotAlertType = rule.type;

        issue.issueCreationTime = Instant.now();
        issue.resolution = IssueResolution.OPEN;

        repository.persist(issue);
    }

    public boolean existsOpenIssue(String ruleId, Long orderId) {
        return repository.find("alertId = ?1 and issueRelatedOrderId = ?2 and resolution = ?3",
                ruleId, orderId, IssueResolution.OPEN).count() > 0;
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
        return repository.list("resolution", Sort.descending("issueCreationTime"), IssueResolution.OPEN);
    }

    public List<AlertIssue> getByOrder(Long orderId) {
        return repository.list("issueRelatedOrderId", Sort.descending("issueCreationTime"), orderId);
    }
}