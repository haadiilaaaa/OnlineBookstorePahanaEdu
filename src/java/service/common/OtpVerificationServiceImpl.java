// service.common.OtpVerificationServiceImpl.java
package service.common;

import dao.OtpTokenDAO;
import model.OtpToken;
import java.time.LocalDateTime;

public class OtpVerificationServiceImpl implements OtpVerificationService {

    private final OtpTokenDAO otpTokenDAO;
    private final UserVerificationStrategyContext strategyContext;

    public OtpVerificationServiceImpl(OtpTokenDAO otpTokenDAO,
                                      UserVerificationStrategyContext strategyContext) {
        this.otpTokenDAO = otpTokenDAO;
        this.strategyContext = strategyContext;
    }

    @Override
    public boolean verifyOtp(String userId, String userType, String enteredOtp) throws Exception {
        OtpToken otp = otpTokenDAO.findValidToken(userId, userType, enteredOtp);

        if (otp == null || otp.isUsed() || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        otpTokenDAO.markUsed(otp.getId());
        strategyContext.verify(userType, userId); // ✅ Strategy call
        return true;
    }
}
