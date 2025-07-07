package service.common;

import dto.UserSession;

public interface LoginService {
    UserSession authenticate(String usernameOrEmail, String password) throws Exception;
}
