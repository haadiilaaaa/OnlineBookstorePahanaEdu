package service.common;

public class LoginServiceFactory {
    public static LoginService createLoginService() {
        try {
            return new LoginServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create LoginServiceImpl", e);
        }
    }
}
