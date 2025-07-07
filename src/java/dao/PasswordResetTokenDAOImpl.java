package dao;
import model.PasswordResetToken;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {

    private final Connection conn;

    public PasswordResetTokenDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(PasswordResetToken token) throws Exception {
        String sql = """
            INSERT INTO password_reset_tokens 
            (id, user_id, user_type, token, expires_at, used, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token.getId() != null ? token.getId() : UUID.randomUUID().toString());
            ps.setString(2, token.getUserId());
            ps.setString(3, token.getUserType());
            ps.setString(4, token.getToken());
            ps.setTimestamp(5, Timestamp.valueOf(token.getExpiresAt()));
            ps.setBoolean(6, token.isUsed());
            ps.setTimestamp(7, Timestamp.valueOf(token.getCreatedAt()));
            ps.executeUpdate();
        }
    }

    @Override
    public PasswordResetToken findByToken(String tokenStr) throws Exception {
        String sql = """
            SELECT * FROM password_reset_tokens 
            WHERE token = ? AND used = FALSE
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenStr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PasswordResetToken token = new PasswordResetToken();
                    token.setId(rs.getString("id"));
                    token.setUserId(rs.getString("user_id"));
                    token.setUserType(rs.getString("user_type"));
                    token.setToken(rs.getString("token"));
                    token.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
                    token.setUsed(rs.getBoolean("used"));
                    token.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return token;
                }
            }
        }
        return null;
    }

    @Override
    public void markAsUsed(String tokenStr) throws Exception {
        String sql = "UPDATE password_reset_tokens SET used = TRUE WHERE token = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenStr);
            ps.executeUpdate();
        }
    }
}
