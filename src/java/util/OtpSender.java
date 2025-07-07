package util;
public interface OtpSender {
    void sendOTP(String toEmail, String otpCode) throws Exception;
}