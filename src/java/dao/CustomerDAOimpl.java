package dao;

import db.DBConnection;
import model.Customer;
import util.*;
import java.sql.*;

public class CustomerDAOimpl implements CustomerDAO {

    private final Connection connection;

    public CustomerDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
public void save(Customer customer) throws Exception {
    String sql = "INSERT INTO customer (id, username, first_name, last_name, email, contact_number, address, password_hash) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, customer.getId());
        stmt.setString(2, customer.getUsername());
        stmt.setString(3, customer.getFirstName());
        stmt.setString(4, customer.getLastName());
        stmt.setString(5, customer.getEmail());
        stmt.setString(6, customer.getContactNumber());
        stmt.setString(7, customer.getAddress());
        stmt.setString(8, customer.getPasswordHash());

        stmt.executeUpdate();
    } catch (SQLIntegrityConstraintViolationException e) {
        if (e.getMessage().contains("customer.username")) {
            throw new ValidationException("Username already exists.");
        } else if (e.getMessage().contains("customer.email")) {
            throw new ValidationException("Email already exists.");
        } else {
            throw new ValidationException("Duplicate entry detected.");
        }
    } catch (SQLException e) {
        e.printStackTrace(); // You can log this instead
        throw new Exception("Database error while saving customer.", e);
    }
}


    @Override
    public Customer findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM customer WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public Customer findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM customer WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public int countCustomers() throws Exception {
        String sql = "SELECT COUNT(*) FROM customer";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public void verify(String userId) throws Exception {
        String sql = "UPDATE customer SET is_verified = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }

    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getString("id"));
        customer.setUsername(rs.getString("username"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setContactNumber(rs.getString("contact_number"));
        customer.setAddress(rs.getString("address"));
        customer.setPasswordHash(rs.getString("password_hash"));
        customer.setVerified(rs.getBoolean("is_verified"));
        return customer;
    }
    public int getMaxCustomerIdNumber() throws SQLException {
    String prefix = "cus__";
    String sql = "SELECT MAX(CAST(SUBSTRING(id, LENGTH(?) + 1) AS UNSIGNED)) AS max_id FROM customer WHERE id LIKE ?";

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
public Customer findById(String id) throws Exception {
    String sql = "SELECT * FROM customer WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return extractCustomerFromResultSet(rs);
        }
    }
    return null;
}
@Override
public Customer findByUsernameOrEmail(String input) throws Exception {
    String sql = "SELECT * FROM customer WHERE username = ? OR email = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, input);
        ps.setString(2, input);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return extractCustomerFromResultSet(rs);
        }
    }
    return null;
}
@Override
public void updatePassword(String userId, String hashedPassword) throws Exception {
    String sql = "UPDATE customer SET password_hash = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, hashedPassword);
        stmt.setString(2, userId);
        stmt.executeUpdate();
    }
}


}
//Data Access implementation for customer