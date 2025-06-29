package service.common;

public interface OtpSendService {
    void sendOtp(String userId, String userType, String email) throws Exception;
}
