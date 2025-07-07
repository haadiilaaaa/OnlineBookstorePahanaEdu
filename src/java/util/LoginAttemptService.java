package util;

import java.util.concurrent.ConcurrentHashMap;

public class LoginAttemptService {
    private final ConcurrentHashMap<String, AttemptInfo> attemptCache = new ConcurrentHashMap<>();
    private final int maxAttempts;
    private final long blockDurationMs;

    public LoginAttemptService(int maxAttempts, long blockDurationMs) {
        this.maxAttempts = maxAttempts;
        this.blockDurationMs = blockDurationMs;
    }

    public boolean isBlocked(String username) {
        AttemptInfo attempt = attemptCache.get(username);
        return attempt != null && System.currentTimeMillis() < attempt.blockedUntil;
    }

    public long getRemainingBlockTime(String username) {
        AttemptInfo attempt = attemptCache.get(username);
        if (attempt == null) return 0;
        return Math.max(0, attempt.blockedUntil - System.currentTimeMillis());
    }

    public void recordFailure(String username) {
        AttemptInfo attempt = attemptCache.getOrDefault(username, new AttemptInfo(0, 0));
        attempt.count++;
        if (attempt.count >= maxAttempts) {
            attempt.blockedUntil = System.currentTimeMillis() + blockDurationMs;
        }
        attemptCache.put(username, attempt);
    }

    public void resetAttempts(String username) {
        attemptCache.remove(username);
    }

    private static class AttemptInfo {
        int count;
        long blockedUntil;

        AttemptInfo(int count, long blockedUntil) {
            this.count = count;
            this.blockedUntil = blockedUntil;
        }
    }
}
