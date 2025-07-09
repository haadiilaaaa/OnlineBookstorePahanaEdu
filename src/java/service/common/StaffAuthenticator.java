package service.common;

import dao.StaffDAO;
import dto.UserSession;
import model.Staff;
import util.PasswordHasher;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(StaffAuthenticator.class.getName());
    private final StaffDAO staffDAO;

    public StaffAuthenticator(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    @Override
    public Optional<UserSession> authenticate(String usernameOrEmail, String password) {
        try {
            return staffDAO.findByUsernameOrEmail(usernameOrEmail)
                    .filter(staff -> PasswordHasher.verifyPassword(password, staff.getPasswordHash()))
                    .map(staff -> new UserSession(
                            staff.getId(),
                            staff.getUsername(),
                            staff.getEmail(),
                            "staff",
                            staff.getFirstName(),
                            staff.getLastName(),
                            staff.getContactNumber(),
                            null
                    ));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Staff authentication failed", e);
            return Optional.empty();
        }
    }
}
