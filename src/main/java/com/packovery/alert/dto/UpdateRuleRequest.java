package com.packovery.alert.dto;

import com.packovery.common.enums.AlertType;

public class UpdateRuleRequest {
    public String name;
    public String description;
    public AlertType type;
    public String threshold;
}
