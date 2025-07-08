// =========================
// ResetRateLimiter.java
// =========================
package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResetRateLimiter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MS = 15 * 60 * 1000; // 15 minutes

    private static final Map<String, AttemptData> attempts = new ConcurrentHashMap<>();

    public static boolean tryAcquire(String key) {
        AttemptData data = attempts.getOrDefault(key, new AttemptData());
        if (data.isBlocked()) return false;

        data.increment();
        attempts.put(key, data);
        return true;
    }

    public static long getRetryAfter(String key) {
        AttemptData data = attempts.get(key);
        return data != null ? data.getRemainingBlockTime() : 0;
    }

    public static void reset(String key) {
        attempts.remove(key);
    }

    static class AttemptData {
        int count = 0;
        long firstAttemptTime = System.currentTimeMillis();

        void increment() {
            count++;
        }

        boolean isBlocked() {
            return count >= MAX_ATTEMPTS &&
                   (System.currentTimeMillis() - firstAttemptTime) < BLOCK_DURATION_MS;
        }

        long getRemainingBlockTime() {
            if (!isBlocked()) return 0;
            return BLOCK_DURATION_MS - (System.currentTimeMillis() - firstAttemptTime);
        }
    }
}
