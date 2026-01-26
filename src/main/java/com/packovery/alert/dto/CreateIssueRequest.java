package com.packovery.alert.dto;

import com.packovery.common.enums.AlertType;

public class CreateIssueRequest {
    public String ruleId;
    public Long orderId;
    public String alertName;
    public AlertType type;
}