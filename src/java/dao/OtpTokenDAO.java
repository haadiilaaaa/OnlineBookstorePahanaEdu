package dao;

import model.OtpToken;

public interface OtpTokenDAO {
    void save(OtpToken token) throws Exception;
    OtpToken findValidToken(String userId, String userType, String otpCode) throws Exception;
    void markUsed(String tokenId) throws Exception;
}
