// service/common/ResetPasswordServiceImpl.java
package service.common;

import dao.PasswordResetTokenDAO;
import dao.PasswordUpdatabale;
import model.PasswordResetToken;
import util.MessageResolver;
import util.PasswordHasher;
import util.ResetRateLimiter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final PasswordResetTokenDAO tokenDAO;
    private final Map<String, PasswordUpdatabale> userPasswordServices;

    public ResetPasswordServiceImpl(PasswordResetTokenDAO tokenDAO, Map<String, PasswordUpdatabale> userPasswordServices) {
        this.tokenDAO = tokenDAO;
        this.userPasswordServices = userPasswordServices;
    }

    @Override
    public boolean resetPassword(String token, String newPassword, String confirmPassword) throws Exception {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException(MessageResolver.get("reset.token.missing"));
        }

        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException(MessageResolver.get("reset.password.mismatch"));
        }

        String rateLimitKey = "reset:" + token;
        if (!ResetRateLimiter.tryAcquire(rateLimitKey)) {
            long waitSec = ResetRateLimiter.getRetryAfter(rateLimitKey) / 1000;
            throw new IllegalStateException(MessageResolver.get("reset.rate_limited", waitSec));
        }

        PasswordResetToken resetToken = tokenDAO.findByToken(token);

        if (resetToken == null || resetToken.isUsed()) {
            throw new IllegalStateException(MessageResolver.get("reset.token.invalid"));
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            Duration expiredDuration = Duration.between(resetToken.getExpiresAt(), LocalDateTime.now());
            long minutesAgo = expiredDuration.toMinutes();
            throw new IllegalStateException(MessageResolver.get("reset.token.expired", minutesAgo));
        }

        String userType = resetToken.getUserType().toLowerCase();
        String userId = resetToken.getUserId();
        PasswordUpdatabale dao = userPasswordServices.get(userType);

        if (dao == null) {
            throw new IllegalStateException(MessageResolver.get("reset.unsupported_user", userType));
        }

        String hashed = PasswordHasher.hashPassword(newPassword);
        dao.updatePassword(userId, hashed);
        tokenDAO.markAsUsed(token);
        ResetRateLimiter.reset(rateLimitKey);

        return true;
    }
}
