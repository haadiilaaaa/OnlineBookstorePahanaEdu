package dao;

import model.Staff;

import java.sql.*;

public class StaffDAOImpl implements StaffDAO {

    private final Connection connection;

    public StaffDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Staff staff) throws Exception {
        String sql = "INSERT INTO staff (id, username, first_name, last_name, email, contact_number, password_hash, is_verified) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, staff.getId());
            ps.setString(2, staff.getUsername());
            ps.setString(3, staff.getFirstName());
            ps.setString(4, staff.getLastName());
            ps.setString(5, staff.getEmail());
            ps.setString(6, staff.getContactNumber());
            ps.setString(7, staff.getPasswordHash());
            ps.setBoolean(8, staff.isVerified());
            ps.executeUpdate();
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
}
//data access implementation for staff