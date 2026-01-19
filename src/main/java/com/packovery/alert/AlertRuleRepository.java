package com.packovery.alert;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlertRuleRepository implements PanacheMongoRepository<AlertRule> {
}
