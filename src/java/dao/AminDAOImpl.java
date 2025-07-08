package dao;

import model.Admin;

import java.sql.*;
import util.*;
public class AminDAOImpl implements AdminDAO, GenericUserDAO,PasswordUpdatabale  {

    private final Connection connection;

    public AminDAOImpl(Connection connection) {
        this.connection = connection;
    }

   @Override
public void save(Admin admin) throws Exception {
   String sql = "INSERT INTO admin (id, username, first_name, last_name, email, contact_number, password_hash, is_verified) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, admin.getId());
        stmt.setString(2, admin.getUsername());
        stmt.setString(3, admin.getFirstName());
        stmt.setString(4, admin.getLastName());
        stmt.setString(5, admin.getEmail());
        stmt.setString(6, admin.getContactNumber());
        stmt.setString(7, admin.getPasswordHash());
        stmt.setBoolean(8, admin.isVerified());
        stmt.executeUpdate();
    } catch (SQLIntegrityConstraintViolationException e) {
        if (e.getMessage().contains("admin.username")) {
            throw new ValidationException("Username already exists.");
        } else if (e.getMessage().contains("admin.email")) {
            throw new ValidationException("Email already exists.");
        } else {
            throw new ValidationException("Duplicate entry detected.");
        }
    } catch (SQLException e) {
        throw new Exception("Database error while saving admin.", e);
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
    public int getMaxAdminIdNumber() throws SQLException {
    String prefix = "ad__";
    String sql = "SELECT MAX(CAST(SUBSTRING(id, LENGTH(?) + 1) AS UNSIGNED)) AS max_id FROM admin WHERE id LIKE ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, prefix);
        stmt.setString(2, prefix + "%");

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("max_id");  // Returns 0 if no existing IDs
        }
    }
    return 0;
}

    @Override
public Admin findById(String id) throws Exception {
    String sql = "SELECT * FROM admin WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return extractAdminFromResultSet(rs);
        }
    }
    return null;
}
@Override
public Admin findByUsernameOrEmail(String input) throws Exception {
    String sql = "SELECT * FROM admin WHERE username = ? OR email = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, input);
        ps.setString(2, input);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return extractAdminFromResultSet(rs);
        }
    }
    return null;
}


    @Override
    public void updatePassword(String userId, String hashedPassword) throws Exception {
        String sql = "UPDATE admin SET password_hash = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }
}



