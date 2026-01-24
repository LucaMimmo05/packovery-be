package com.packovery.logging.dto;

import com.packovery.common.enums.ActionType;
import com.packovery.common.enums.EntityViewed;

import java.util.Map;

public class CreateUserLogRequest {
    public Long userId;
    public ActionType actionType;
    public EntityViewed entityViewed;
    public Map<String, Object> metadata;
}
