package service.common;

import dao.*;
import model.OtpToken;

import java.time.LocalDateTime;

public class OtpVerificationServiceImpl implements OtpVerificationService {

    private final OtpTokenDAO otpTokenDAO;
    private final CustomerDAO customerDAO;
    private final AdminDAO adminDAO;
    private final StaffDAO staffDAO;

    public OtpVerificationServiceImpl(OtpTokenDAO otpTokenDAO,
                                      CustomerDAO customerDAO,
                                      AdminDAO adminDAO,
                                      StaffDAO staffDAO) {
        this.otpTokenDAO = otpTokenDAO;
        this.customerDAO = customerDAO;
        this.adminDAO = adminDAO;
        this.staffDAO = staffDAO;
        
    }

    @Override
    
    public boolean verifyOtp(String userId, String userType, String enteredOtp) throws Exception {
        

        OtpToken otp = otpTokenDAO.findValidToken(userId, userType, enteredOtp);
System.out.println("Found token: " + (otp != null ? otp.getOtpCode() : "null"));

        if (otp == null || otp.isUsed() || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        otpTokenDAO.markUsed(otp.getId()); // mark OTP as used

        switch (userType) {
            case "customer" -> customerDAO.verify(userId);
            case "admin" -> adminDAO.verify(userId);
            case "staff" -> staffDAO.verify(userId);
            default -> throw new IllegalArgumentException("Unknown user type");
        }

        return true;
    }
}
