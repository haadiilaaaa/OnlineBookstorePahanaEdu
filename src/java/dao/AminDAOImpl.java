package dao;

import model.Admin;

import java.sql.*;

public class AminDAOImpl implements AdminDAO {

    private final Connection connection;

    public AminDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Admin admin) throws Exception {
        String sql = "INSERT INTO admin (id, username, first_name, last_name, email, contact_number, password_hash, is_verified) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, admin.getId());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getFirstName());
            ps.setString(4, admin.getLastName());
            ps.setString(5, admin.getEmail());
            ps.setString(6, admin.getContactNumber());
            ps.setString(7, admin.getPasswordHash());
            ps.setBoolean(8, admin.isVerified());
            ps.executeUpdate();
        }
    }

    @Override
    public Admin findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM admin WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public Admin findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public int countAdmins() throws Exception {
        String sql = "SELECT COUNT(*) FROM admin";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public void verify(String userId) throws Exception {
        String sql = "UPDATE admin SET is_verified = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }

    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getString("id"));
        admin.setUsername(rs.getString("username"));
        admin.setFirstName(rs.getString("first_name"));
        admin.setLastName(rs.getString("last_name"));
        admin.setEmail(rs.getString("email"));
        admin.setContactNumber(rs.getString("contact_number"));
        admin.setPasswordHash(rs.getString("password_hash"));
        admin.setVerified(rs.getBoolean("is_verified"));
        return admin;
    }
}
