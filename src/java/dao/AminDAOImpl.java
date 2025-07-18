package dao;

import model.Admin;
import util.DAOExeption;
import util.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Optional;
import java.util.logging.Logger;

import static sql.AdminSQL.*;

public class AminDAOImpl implements AdminDAO, GenericUserDAO<Admin>, PasswordUpdatabale {

    private static final Logger logger = Logger.getLogger(AminDAOImpl.class.getName());
    private final Connection connection;

    public AminDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Admin admin) throws DAOExeption {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_ADMIN)) {
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
            logger.warning("Duplicate entry: " + e.getMessage());
            String msg = e.getMessage().contains("username") ? "Username already exists."
                       : e.getMessage().contains("email")    ? "Email already exists."
                       : "Duplicate entry.";
            throw new DAOExeption(msg, e);
        } catch (SQLException e) {
            logger.severe("Failed to save admin: " + e.getMessage());
            throw new DAOExeption("Database error while saving admin.", e);
        }
    }

    @Override
    public Optional<Admin> findByEmail(String email) throws DAOExeption {
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOExeption("Failed to find admin by email", e);
        }
    }

    @Override
    public Optional<Admin> findByUsername(String username) throws DAOExeption {
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOExeption("Failed to find admin by username", e);
        }
    }

    @Override
    public Optional<Admin> findById(String id) throws DAOExeption {
        try (PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOExeption("Failed to find admin by ID", e);
        }
    }

    @Override
    public Optional<Admin> findByUsernameOrEmail(String input) throws DAOExeption {
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_USERNAME_OR_EMAIL)) {
            ps.setString(1, input);
            ps.setString(2, input);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOExeption("Failed to find admin by username/email", e);
        }
    }

    @Override
    public int countAdmins() throws DAOExeption {
        try (PreparedStatement ps = connection.prepareStatement(COUNT_ADMINS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOExeption("Failed to count admins", e);
        }
    }

    @Override
    public void verify(String userId) throws DAOExeption {
        try (PreparedStatement stmt = connection.prepareStatement(VERIFY_ADMIN)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExeption("Failed to verify admin", e);
        }
    }

    @Override
    public void updatePassword(String userId, String hashedPassword) throws DAOExeption {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_PASSWORD)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExeption("Failed to update password", e);
        }
    }

    @Override
    public int getMaxAdminIdNumber() throws DAOExeption {
        try (PreparedStatement stmt = connection.prepareStatement(GET_MAX_ADMIN_ID)) {
            stmt.setString(1, "ad__");
            stmt.setString(2, "ad__%");
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("max_id") : 0;
            }
        } catch (SQLException e) {
            throw new DAOExeption("Failed to get max admin ID", e);
        }
    }

    private Admin map(ResultSet rs) throws SQLException {
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
    @Override
public List<String> findAllAdminEmails() throws DAOExeption {
    List<String> emails = new ArrayList<>();
    String query = "SELECT email FROM admin WHERE is_verified = 1"; // Only verified admins

    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            emails.add(rs.getString("email"));
        }

    } catch (SQLException e) {
        throw new DAOExeption("Failed to fetch admin emails", e);
    }

    return emails;
}

@Override
public void update(Admin admin) throws DAOExeption {
    String sql = "UPDATE admin SET username = ?, first_name = ?, last_name = ?, email = ?, contact_number = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, admin.getUsername());
        stmt.setString(2, admin.getFirstName());
        stmt.setString(3, admin.getLastName());
        stmt.setString(4, admin.getEmail());
        stmt.setString(5, admin.getContactNumber());
        stmt.setString(6, admin.getId());
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new DAOExeption("Failed to update admin profile", e);
    }
}


}
