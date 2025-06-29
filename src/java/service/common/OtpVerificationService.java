package service.common;

public interface OtpVerificationService {
    boolean verifyOtp(String userId, String userType, String enteredOtp) throws Exception;
}
