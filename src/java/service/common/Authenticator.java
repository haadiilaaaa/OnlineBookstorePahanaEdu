package service.common;

import dto.UserSession;

import java.util.Optional;

public interface Authenticator {
    Optional<UserSession> authenticate(String usernameOrEmail, String password);
}
