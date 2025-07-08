package service.common;

import dao.OtpTokenDAO;
import model.OtpToken;
import util.contannts.OTPConstants;

import java.time.LocalDateTime;
import java.util.logging.Logger;

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

        otpTokenDAO.markUsed(otp.getId());
        strategyContext.verify(userType, userId);

        logger.info(OTPConstants.OTP_VERIFIED_SUCCESS);
        return true;
    }
}
