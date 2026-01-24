package com.packovery.alert.dto;

import com.packovery.common.enums.AlertType;

public class CreateRuleRequest {
    public String name;
    public String description;
    public AlertType type;
    public String threshold;
}