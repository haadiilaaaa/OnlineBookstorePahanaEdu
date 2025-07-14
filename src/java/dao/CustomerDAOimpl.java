package dao;

import db.DBConnection;
import model.Customer;
import util.ValidationException;
import util.*;
import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDAOimpl implements CustomerDAO {

    private static final Logger logger = Logger.getLogger(CustomerDAOimpl.class.getName());

    private static final String TABLE = "customer";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_CONTACT_NUMBER = "contact_number";
    private static final String COL_ADDRESS = "address";
    private static final String COL_PASSWORD_HASH = "password_hash";
    private static final String COL_IS_VERIFIED = "is_verified";

    private final Connection connection;

    public CustomerDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Customer customer) throws DAOExeption {
        String sql = "INSERT INTO " + TABLE + " (" + COL_ID + ", " + COL_USERNAME + ", " + COL_FIRST_NAME + ", "
                + COL_LAST_NAME + ", " + COL_EMAIL + ", " + COL_CONTACT_NUMBER + ", " + COL_ADDRESS + ", " + COL_PASSWORD_HASH + ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            if (e.getMessage().contains(TABLE + "." + COL_USERNAME)) {
                throw new ValidationException("Username already exists.");
            } else if (e.getMessage().contains(TABLE + "." + COL_EMAIL)) {
                throw new ValidationException("Email already exists.");
            } else {
                throw new ValidationException("Duplicate entry detected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while saving customer", e);
            throw new DAOExeption("Database error while saving customer.", e);
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_EMAIL + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractCustomerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding customer by email", e);
            throw new DAOExeption("Error finding customer by email.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByUsername(String username) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_USERNAME + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractCustomerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding customer by username", e);
            throw new DAOExeption("Error finding customer by username.", e);
        }
        return Optional.empty();
    }

    @Override
    public int countCustomers() throws DAOExeption {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error counting customers", e);
            throw new DAOExeption("Error counting customers.", e);
        }
        return 0;
    }

    @Override
    public void verify(String userId) throws DAOExeption {
        String sql = "UPDATE " + TABLE + " SET " + COL_IS_VERIFIED + " = TRUE WHERE " + COL_ID + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error verifying customer", e);
            throw new DAOExeption("Error verifying customer.", e);
        }
    }

    @Override
    public int getMaxCustomerIdNumber() throws DAOExeption {
        String prefix = "cus__";
        String sql = "SELECT MAX(CAST(SUBSTRING(" + COL_ID + ", LENGTH(?) + 1) AS UNSIGNED)) AS max_id FROM " + TABLE + " WHERE " + COL_ID + " LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, prefix);
            stmt.setString(2, prefix + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_id");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting max customer ID number", e);
            throw new DAOExeption("Error getting max customer ID number.", e);
        }
        return 0;
    }

    @Override
    public Optional<Customer> findById(String id) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_ID + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractCustomerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding customer by id", e);
            throw new DAOExeption("Error finding customer by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByUsernameOrEmail(String input) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_USERNAME + " = ? OR " + COL_EMAIL + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, input);
            ps.setString(2, input);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractCustomerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding customer by username or email", e);
            throw new DAOExeption("Error finding customer by username or email.", e);
        }
        return Optional.empty();
    }

    @Override
    public void updatePassword(String userId, String hashedPassword) throws DAOExeption {
        String sql = "UPDATE " + TABLE + " SET " + COL_PASSWORD_HASH + " = ? WHERE " + COL_ID + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating customer password", e);
            throw new DAOExeption("Error updating customer password.", e);
        }
    }

    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getString(COL_ID));
        customer.setUsername(rs.getString(COL_USERNAME));
        customer.setFirstName(rs.getString(COL_FIRST_NAME));
        customer.setLastName(rs.getString(COL_LAST_NAME));
        customer.setEmail(rs.getString(COL_EMAIL));
        customer.setContactNumber(rs.getString(COL_CONTACT_NUMBER));
        customer.setAddress(rs.getString(COL_ADDRESS));
        customer.setPasswordHash(rs.getString(COL_PASSWORD_HASH));
        customer.setVerified(rs.getBoolean(COL_IS_VERIFIED));
        return customer;
    }
}
