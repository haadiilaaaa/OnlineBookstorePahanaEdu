package model;
import java.time.LocalDateTime;
public class PasswordResetToken {
    private String id;
    private String userId;
    private String userType;
    private String token;
    private LocalDateTime expiresAt;
    private boolean used;
    private LocalDateTime createdAt;

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
