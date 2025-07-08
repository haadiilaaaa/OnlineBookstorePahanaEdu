
package util;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class OtpRateLimiter {
    private static class Attempt {
        long timestamp;
        int count;

        Attempt(long timestamp, int count) {
            this.timestamp = timestamp;
            this.count = count;
        }
    }

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();
    private final int maxAttempts;
    private final long timeWindowMillis;

    public OtpRateLimiter(int maxAttempts, long timeWindowMillis) {
        this.maxAttempts = maxAttempts;
        this.timeWindowMillis = timeWindowMillis;
    }

    public synchronized boolean tryAcquire(String key) {
        long now = System.currentTimeMillis();
        Attempt attempt = attempts.get(key);

        if (attempt == null || now - attempt.timestamp > timeWindowMillis) {
            // Reset count
            attempts.put(key, new Attempt(now, 1));
            return true;
        }

        if (attempt.count < maxAttempts) {
            attempt.count++;
            return true;
        }

        // Rate limit exceeded
        return false;
    }

    public long getRetryAfter(String key) {
        Attempt attempt = attempts.get(key);
        if (attempt == null) return 0;
        long elapsed = System.currentTimeMillis() - attempt.timestamp;
        return timeWindowMillis - elapsed;
    }
}
