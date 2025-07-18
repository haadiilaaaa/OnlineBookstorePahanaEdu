package service.common;

import dao.OtpTokenDAO;
import model.OtpToken;
import util.contannts.OTPConstants;
import dao.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import db.DBConnection;
import util.*;
import java.util.List;

public class OtpVerificationServiceImpl implements OtpVerificationService {

    private static final Logger logger = Logger.getLogger(OtpVerificationServiceImpl.class.getName());

    private final OtpTokenDAO otpTokenDAO;
    private final UserVerificationStrategyContext strategyContext;

    public OtpVerificationServiceImpl(OtpTokenDAO otpTokenDAO,
                                      UserVerificationStrategyContext strategyContext) {
        this.otpTokenDAO = otpTokenDAO;
        this.strategyContext = strategyContext;
    }

  @Override
public boolean verifyOtp(String userId, String userType, String enteredOtp) throws Exception {
    logger.info(String.format(OTPConstants.OTP_VERIFICATION_ATTEMPT, userId, userType));

    OtpToken otp = otpTokenDAO.findValidToken(userId, userType, enteredOtp);

    if (otp == null) {
        logger.warning(OTPConstants.OTP_NOT_FOUND_OR_INVALID);
        return false;
    }
    if (otp.isUsed()) {
        logger.warning(OTPConstants.OTP_ALREADY_USED);
        return false;
    }
    if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
        logger.warning(OTPConstants.OTP_EXPIRED);
        return false;
    }

    // ✅ Mark OTP as used
    otpTokenDAO.markUsed(otp.getId());

    // ✅ Mark user as verified in DB
    strategyContext.verify(userType, userId);

    // ✅ Send approval email to admins if it's a delivery partner
    if ("delivery".equalsIgnoreCase(userType)) {
        try {
            notifyAdminsOfPendingDeliveryPartner(userId);
        } catch (Exception e) {
            logger.warning("Failed to notify admins about delivery partner approval: " + e.getMessage());
        }
    }

    logger.info(OTPConstants.OTP_VERIFIED_SUCCESS);
    return true;
}

private void notifyAdminsOfPendingDeliveryPartner(String deliveryPartnerId) throws Exception {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            AdminDAO adminDAO = new AminDAOImpl(conn);
            List<String> adminEmails = adminDAO.findAllAdminEmails();

            if (adminEmails.isEmpty()) {
                logger.warning("No admin emails found.");
                return;
            }

            EmailSender emailSender = EmailServiceFactory.createGeneralEmailService();

            for (String email : adminEmails) {
                String subject = "Approval Needed: Delivery Partner Registered";
                String body = "A new delivery partner with ID <b>" + deliveryPartnerId + "</b> has verified their OTP. " +
                              "Please log in to the admin dashboard to approve or reject.";

                emailSender.sendEmail(email, subject, body);
                logger.info("Sending admin email to: " + email);
logger.info("Email subject: " + subject);
logger.info("Email body: " + body);

            }

            logger.info("Admin notification email sent to " + adminEmails.size() + " admins.");
            
        }
    }
}


