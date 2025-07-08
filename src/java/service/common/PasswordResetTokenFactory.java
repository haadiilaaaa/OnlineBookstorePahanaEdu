
package service.common;
import model.*;
import java.util.UUID;
import java.util.Date;
import java.time.LocalDateTime;


public class PasswordResetTokenFactory {
    public static PasswordResetToken createToken(String userId, String userType) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setId(UUID.randomUUID().toString());
        resetToken.setUserId(userId);
        resetToken.setUserType(userType);
        resetToken.setToken(token);
        resetToken.setExpiresAt(expiresAt);
        resetToken.setCreatedAt(now);
        resetToken.setUsed(false);

        return resetToken;
    }
}
