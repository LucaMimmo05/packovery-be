package com.packovery.alert;

import com.packovery.common.enums.AlertStatus;
import com.packovery.common.enums.AlertType;
import com.packovery.common.enums.OrderStatus;
import com.packovery.order.Order;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AlertEngine {

    @Inject
    AlertRuleService ruleService;

    @Inject
    AlertIssueService issueService;

    @Scheduled(every = "60s")
    @Transactional
    public void checkAllRules() {
        List<AlertRule> rules = ruleService.getAllRules();

        for (AlertRule rule : rules) {
            if (rule.status != AlertStatus.ACTIVE) {
                continue;
            }

            if (rule.type == AlertType.DELAY_START) {
                checkDelayStart(rule);
            }
        }
    }

    private void checkDelayStart(AlertRule rule) {
        long thresholdMinutes = parseThresholdToMinutes(rule.timeThreshold);

        if (thresholdMinutes <= 0) return;

        List<Order> pendingOrders = Order.list("status", OrderStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();

        for (Order order : pendingOrders) {
            if (order.getCreationDate() == null) continue;

            long minutesElapsed = Duration.between(order.getCreationDate(), now).toMinutes();

            if (minutesElapsed > thresholdMinutes) {
                issueService.createIssue(rule.id.toString(), order.id);
            }
        }
    }

    private long parseThresholdToMinutes(String timeString) {
        try {
            if (timeString == null || !timeString.contains(":")) return 0;
            String[] parts = timeString.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return (hours * 60L) + minutes;
        } catch (Exception e) {
            return 0;
        }
    }
}