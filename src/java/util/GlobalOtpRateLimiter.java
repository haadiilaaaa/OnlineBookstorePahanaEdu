package util;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalOtpRateLimiter {
    private static final OtpRateLimiter instance = new OtpRateLimiter(3, 10 * 60 * 1000); // 3 attempts per 10 mins

    private GlobalOtpRateLimiter() {} // Prevent instantiation

    public static OtpRateLimiter getInstance() {
        return instance;
    }
}
