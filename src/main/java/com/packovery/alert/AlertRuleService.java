package com.packovery.alert;

import com.packovery.common.enums.AlertStatus;
import com.packovery.common.enums.AlertType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;
import java.util.List;

@ApplicationScoped
public class AlertRuleService {

    @Inject
    AlertRuleRepository repository;

    @Transactional
    public void createRule(String name, String description, AlertType type, String threshold) {
        AlertRule rule = new AlertRule();
        rule.name = name;
        rule.description = description;
        rule.type = type;
        rule.timeThreshold = threshold;
        rule.status = AlertStatus.ACTIVE;
        repository.persist(rule);
    }

    public List<AlertRule> getAllRules() {
        return repository.listAll();
    }

    @Transactional
    public void toggleStatus(String id, AlertStatus status) {
        AlertRule rule = repository.findById(new ObjectId(id));
        if(rule != null) {
            rule.status = status;
            repository.update(rule);
        }
    }

    @Transactional
    public void deleteRule(String id) {
        repository.deleteById(new ObjectId(id));
    }
}