package util.contannts;

import java.util.concurrent.TimeUnit;
import util.OtpRateLimiter;

public class SecurityConstants {
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final long BLOCK_DURATION_MS = TimeUnit.MINUTES.toMillis(15); // 15 minutes
    private static final int MAX_ATTEMPTS = 3;
private static final long TIME_WINDOW_MILLIS = TimeUnit.MINUTES.toMillis(10);

private OtpRateLimiter otpRateLimiter = new OtpRateLimiter(MAX_ATTEMPTS, TIME_WINDOW_MILLIS);

}
