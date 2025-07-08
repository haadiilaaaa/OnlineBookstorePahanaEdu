package dao;

import model.Staff;

import java.sql.*;
import model.Customer;
import util.*;
//
public class StaffDAOImpl implements StaffDAO, GenericUserDAO ,PasswordUpdatabale {

    private final Connection connection;

    public StaffDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
public void save(Staff staff) throws Exception {
    String sql = "INSERT INTO staff (id, username, first_name, last_name, email, contact_number, password_hash, is_verified) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, staff.getId());
        stmt.setString(2, staff.getUsername());
        stmt.setString(3, staff.getFirstName());
        stmt.setString(4, staff.getLastName());
        stmt.setString(5, staff.getEmail());
        stmt.setString(6, staff.getContactNumber());
        stmt.setString(7, staff.getPasswordHash());
        stmt.setBoolean(8, staff.isVerified());
        stmt.executeUpdate();
    } catch (SQLIntegrityConstraintViolationException e) {
        if (e.getMessage().contains("staff.username")) {
            throw new ValidationException("Username already exists.");
        } else if (e.getMessage().contains("staff.email")) {
            throw new ValidationException("Email already exists.");
        } else {
            throw new ValidationException("Duplicate entry detected.");
        }
    } catch (SQLException e) {
        throw new Exception("Database error while saving staff.", e);
    }
}

    @Override
    public Staff findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM staff WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractStaffFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public Staff findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM staff WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractStaffFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public int countStaff() throws Exception {
        String sql = "SELECT COUNT(*) FROM staff";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public void verify(String userId) throws Exception {
        String sql = "UPDATE staff SET is_verified = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }

    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getString("id"));
        staff.setUsername(rs.getString("username"));
        staff.setFirstName(rs.getString("first_name"));
        staff.setLastName(rs.getString("last_name"));
        staff.setEmail(rs.getString("email"));
        staff.setContactNumber(rs.getString("contact_number"));
        staff.setPasswordHash(rs.getString("password_hash"));
        staff.setVerified(rs.getBoolean("is_verified"));
        return staff;
    }
     public int getMaxStaffIdNumber() throws SQLException {
    String prefix = "st__";
    String sql = "SELECT MAX(CAST(SUBSTRING(id, LENGTH(?) + 1) AS UNSIGNED)) AS max_id FROM staff WHERE id LIKE ?";

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
public Staff findById(String id) throws Exception {
    String sql = "SELECT * FROM staff WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return extractStaffFromResultSet(rs);
        }
    }
    return null;
}
@Override
public Staff findByUsernameOrEmail(String input) throws Exception {
    String sql = "SELECT * FROM staff WHERE username = ? OR email = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, input);
        ps.setString(2, input);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return extractStaffFromResultSet(rs);
        }
    }
    return null;
}

    @Override
    public void updatePassword(String userId, String hashedPassword) throws Exception {
        String sql = "UPDATE staff SET password_hash = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }
}
//data access implementation for staff