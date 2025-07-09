package service.common;

import dao.AdminDAO;
import dto.UserSession;
import model.Admin;
import util.PasswordHasher;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(AdminAuthenticator.class.getName());
    private final AdminDAO adminDAO;

    public AdminAuthenticator(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Override
    public Optional<UserSession> authenticate(String usernameOrEmail, String password) {
        try {
            return adminDAO.findByUsernameOrEmail(usernameOrEmail)
                    .filter(admin -> PasswordHasher.verifyPassword(password, admin.getPasswordHash()))
                    .map(admin -> new UserSession(
                            admin.getId(),
                            admin.getUsername(),
                            admin.getEmail(),
                            "admin",
                            admin.getFirstName(),
                            admin.getLastName(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Admin authentication failed", e);
            return Optional.empty();
        }
    }
}
