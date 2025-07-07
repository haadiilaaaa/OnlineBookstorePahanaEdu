package util;

import java.util.Optional;

public class LoginRequestValidator {
    public static Optional<String> validate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.of("Username is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            return Optional.of("Password is required.");
        }
        return Optional.empty();
    }
}
