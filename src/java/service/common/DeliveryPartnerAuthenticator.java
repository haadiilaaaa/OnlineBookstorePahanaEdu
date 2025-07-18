package service.common;

import dao.DeliveryPartnerDAO;
import dto.UserSession;
import model.DeliveryPartner;
import util.PasswordHasher;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryPartnerAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(DeliveryPartnerAuthenticator.class.getName());
    private final DeliveryPartnerDAO deliveryPartnerDAO;

    public DeliveryPartnerAuthenticator(DeliveryPartnerDAO deliveryPartnerDAO) {
        this.deliveryPartnerDAO = deliveryPartnerDAO;
    }

    @Override
    public Optional<UserSession> authenticate(String usernameOrEmail, String password) {
        try {
            return deliveryPartnerDAO.findByUsernameOrEmail(usernameOrEmail)
                    // Only allow approved delivery partners
                    .filter(partner -> "approved".equalsIgnoreCase(partner.getStatus()))
                    // Verify password hash
                    .filter(partner -> PasswordHasher.verifyPassword(password, partner.getPasswordHash()))
                    // Map to UserSession object
                    .map(partner -> new UserSession(
                            partner.getId(),
                            partner.getUsername(),
                            partner.getEmail(),
                            "delivery", // user type string
                            partner.getFirstName(),
                            partner.getLastName(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Delivery Partner authentication failed", e);
            return Optional.empty();
        }
    }
}
