package service.common;

import dto.UserSession;
import service.common.Authenticator;

import java.util.List;
import java.util.Optional;

public class LoginServiceImpl implements LoginService {

    private final List<Authenticator> authenticators;

    public LoginServiceImpl(List<Authenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public UserSession authenticate(String usernameOrEmail, String password) throws Exception {
        for (Authenticator authenticator : authenticators) {
            Optional<UserSession> session = authenticator.authenticate(usernameOrEmail, password);
            if (session.isPresent()) {
                return session.get();
            }
        }
        return null;
    }
}
