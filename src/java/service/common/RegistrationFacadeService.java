package service.common;

import javax.servlet.http.HttpServletRequest;

public interface RegistrationFacadeService {
    String register(String userType, HttpServletRequest request) throws Exception;
}
