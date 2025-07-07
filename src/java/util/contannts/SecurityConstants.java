package util.contannts;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final long BLOCK_DURATION_MS = TimeUnit.MINUTES.toMillis(15); // 15 minutes
}
