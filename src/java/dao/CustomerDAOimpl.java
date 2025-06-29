package dao;

import db.DBConnection;
import model.Customer;

import java.sql.*;

public class CustomerDAOimpl implements CustomerDAO {

    private final Connection connection;

    public CustomerDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Customer customer) throws Exception {
        String sql = "INSERT INTO customer (id, username, first_name, last_name, email, contact_number, address, password_hash, is_verified) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getUsername());
            ps.setString(3, customer.getFirstName());
            ps.setString(4, customer.getLastName());
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getContactNumber());
            ps.setString(7, customer.getAddress());
            ps.setString(8, customer.getPasswordHash());
            ps.setBoolean(9, customer.isVerified());
            ps.executeUpdate();
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
}
