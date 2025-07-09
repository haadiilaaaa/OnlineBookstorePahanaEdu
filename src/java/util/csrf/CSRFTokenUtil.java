package util.csrf;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.math.BigInteger;

public class CSRFTokenUtil {

    private static final String CSRF_SESSION_KEY = "csrfToken";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken(HttpSession session) {
        String token = new BigInteger(130, secureRandom).toString(32);
        session.setAttribute(CSRF_SESSION_KEY, token);
        return token;
    }

    public static boolean isValidToken(HttpSession session, String token) {
        if (session == null || token == null) return false;
        String storedToken = (String) session.getAttribute(CSRF_SESSION_KEY);
        return token.equals(storedToken);
    }
}
