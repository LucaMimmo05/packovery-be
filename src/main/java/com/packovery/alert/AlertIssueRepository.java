package com.packovery.alert;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlertIssueRepository implements PanacheMongoRepository<AlertIssue> {
}
