package dao;

import model.OtpToken;
import java.sql.*;

public class OtpTokenDAOImpl implements OtpTokenDAO {

    private final Connection connection;

    public OtpTokenDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(OtpToken token) throws Exception {
        String sql = "INSERT INTO otp_tokens (id, user_type, user_id, otp_code, expires_at, used, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token.getId());
            ps.setString(2, token.getUserType());
            ps.setString(3, token.getUserId());
            ps.setString(4, token.getOtpCode());
            ps.setTimestamp(5, Timestamp.valueOf(token.getExpiresAt()));
            ps.setBoolean(6, token.isUsed());
            ps.setTimestamp(7, Timestamp.valueOf(token.getCreatedAt()));
            ps.executeUpdate();
        }
    }

    @Override
public OtpToken findValidToken(String userId, String userType, String otpCode) throws Exception {
    String sql = "SELECT * FROM otp_tokens " +
                 "WHERE user_id = ? AND user_type = ? AND otp_code = ? " +
                 "AND used = FALSE AND expires_at > CURRENT_TIMESTAMP " +
                 "ORDER BY created_at DESC LIMIT 1";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, userId.trim());
        ps.setString(2, userType.trim().toLowerCase());
        ps.setString(3, otpCode.trim());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            OtpToken token = new OtpToken();
            token.setId(rs.getString("id"));
            token.setUserId(rs.getString("user_id"));
            token.setUserType(rs.getString("user_type"));
            token.setOtpCode(rs.getString("otp_code"));
            token.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
            token.setUsed(rs.getBoolean("used"));
            token.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return token;
        }
    }
    return null;
}


    @Override
    public void markUsed(String tokenId) throws Exception {
        String sql = "UPDATE otp_tokens SET used = TRUE WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tokenId);
            ps.executeUpdate();
        }
    }
}
