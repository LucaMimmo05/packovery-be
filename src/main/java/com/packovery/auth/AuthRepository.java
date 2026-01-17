package com.packovery.auth;

import com.packovery.common.enums.UserStatus;
import com.packovery.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.time.LocalDateTime;

@ApplicationScoped
public class AuthRepository implements PanacheRepository<User> {

    public User findByEmail(String email) {
        return User.find("email", email).firstResult();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void blockUser(Long userId, int failedAttempts) {
        User user = User.findById(userId);
        if (user == null) return;
        
        user.setFailedAttempts(failedAttempts);

        if (failedAttempts >= 6) {
            user.setAccountStatus(UserStatus.PERM_BLOCKED);
            user.setBlockedUntil(null);
        } else if (failedAttempts >= 5) {
            user.setAccountStatus(UserStatus.TEMP_BLOCKED);
            user.setBlockedUntil(LocalDateTime.now().plusHours(1));
        } else if (failedAttempts >= 3) {
            user.setAccountStatus(UserStatus.TEMP_BLOCKED);
            user.setBlockedUntil(LocalDateTime.now().plusMinutes(30));
        } else {
            user.setAccountStatus(UserStatus.ACTIVE);
            user.setBlockedUntil(null);
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void unblockUser(Long userId) {
        User user = User.findById(userId);
        if (user == null) return;

        user.setAccountStatus(UserStatus.ACTIVE);
        user.setBlockedUntil(null);
        user.setFailedAttempts(0);
    }
}
