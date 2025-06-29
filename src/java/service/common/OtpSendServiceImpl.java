package service.common;

import dao.OtpTokenDAO;
import model.OtpToken;
import util.EmailSender;
import util.OtpGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

public class OtpSendServiceImpl implements OtpSendService {

    private final OtpTokenDAO otpTokenDAO;

    public OtpSendServiceImpl(OtpTokenDAO otpTokenDAO) {
        this.otpTokenDAO = otpTokenDAO;
    }

    @Override
    public void sendOtp(String userId, String userType, String email) throws Exception {
        String otpCode = OtpGenerator.generateOTP();

        OtpToken otpToken = new OtpToken();
        otpToken.setId(UUID.randomUUID().toString());
        otpToken.setUserId(userId);
        otpToken.setUserType(userType);
        otpToken.setOtpCode(otpCode);
        otpToken.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpToken.setUsed(false);
        otpToken.setCreatedAt(LocalDateTime.now());

        otpTokenDAO.save(otpToken);
        EmailSender.sendOTP(email, otpCode);
        System.out.println("OTP saved: " + otpCode + " for userId=" + userId + " userType=" + userType);
        

    }
}
