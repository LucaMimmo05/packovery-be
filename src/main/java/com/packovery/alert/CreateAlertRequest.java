package com.packovery.alert;

import com.packovery.common.enums.AlertType;

public class CreateAlertRequest {
    public Long orderId;
    public String alertName;
    public AlertType type;
}
