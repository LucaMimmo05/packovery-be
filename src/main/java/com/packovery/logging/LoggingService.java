package com.packovery.logging;

import com.packovery.common.enums.ActionType;
import com.packovery.common.enums.EntityViewed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class LoggingService {

    @Inject
    UserLogRepository userLogRepository;

    public void logAction(Long userId, ActionType actionType, EntityViewed entityViewed, Map<String, Object> details) {
        UserLog userLog = new UserLog();
        userLog.userId = userId;
        userLog.actionType = actionType;
        userLog.entityViewed = entityViewed;
        userLog.eventTimestamp = Instant.now();

        if (actionType == ActionType.CREATE) {
           userLog.userSessionStart = Instant.now();
        }

        userLog.metadata = details;

        userLogRepository.persist(userLog);
    }

    public void logMessage(Long senderId, Long riderId, String text) {
        UserLog userLog = new UserLog();
        userLog.userId = senderId;
        userLog.actionType = ActionType.SEND_MESSAGE;
        userLog.entityViewed = EntityViewed.RIDER;
        userLog.eventTimestamp = Instant.now();
        userLog.messageSentTime = Instant.now();
        userLog.messageReadStatus = false;

        userLog.metadata = Map.of(
                "riderId", riderId,
                "text", text
        );

        userLogRepository.persist(userLog);
    }

    public List<UserLog> getLogsByUserId(Long userId) {
        return userLogRepository.findByUserId(userId);
    }

    public void logLogout(Long userId) {
        UserLog lastLogin = userLogRepository.find(
                "userId = ?1 and actionType = ?2 and userSessionEnd is null",
                userId, ActionType.LOGIN).firstResult();

        if(lastLogin != null) {
            lastLogin.userSessionEnd = Instant.now();
            userLogRepository.update(lastLogin);
        }
    }

    public void logAlertCreation(Long userId, String alertTime) {
        UserLog userLog = new UserLog();
        userLog.userId = userId;
        userLog.actionType = ActionType.CREATE;
        userLog.entityViewed = EntityViewed.ALERT;
        userLog.eventTimestamp = Instant.now();
        userLog.alertCreationTime = Instant.now();
        userLog.metadata = Map.of("alertTime", alertTime);

        userLogRepository.persist(userLog);
    }
}
