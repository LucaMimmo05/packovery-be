package com.packovery.user;

import com.packovery.common.enums.UserRole;
import com.packovery.common.enums.UserStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus accountStatus = UserStatus.ACTIVE;

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts = 0;

    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil;


    public User() {}

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.failedAttempts = 0;
    }

    public User(String email, String passwordHash, UserRole role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.failedAttempts = 0;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(UserStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(LocalDateTime blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public static User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public static void blockUser(Long userId, int failedAttempts) {
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
    public static void unblockUser(Long userId) {
        User user = User.findById(userId);
        if (user == null) return;

        user.setAccountStatus(UserStatus.ACTIVE);
        user.setBlockedUntil(null);
        user.setFailedAttempts(0);
    }

    public boolean isBlocked() {
        return accountStatus == UserStatus.TEMP_BLOCKED || accountStatus == UserStatus.PERM_BLOCKED;
    }

    public boolean isTemporaryBlocked() {
        return accountStatus == UserStatus.TEMP_BLOCKED &&
               blockedUntil != null &&
               LocalDateTime.now().isBefore(blockedUntil);
    }

    public boolean isPermanentlyBlocked() {
        return accountStatus == UserStatus.PERM_BLOCKED;
    }

    public boolean canLogin() {
        if (isPermanentlyBlocked()) return false;
        if (isTemporaryBlocked()) return false;
        return accountStatus == UserStatus.ACTIVE;
    }

    public void incrementFailedAttempts() {
        this.failedAttempts++;
        blockUser(this.id, this.failedAttempts);
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
        if (accountStatus == UserStatus.TEMP_BLOCKED &&
            blockedUntil != null &&
            LocalDateTime.now().isAfter(blockedUntil)) {
            this.accountStatus = UserStatus.ACTIVE;
            this.blockedUntil = null;
        }
    }
}
