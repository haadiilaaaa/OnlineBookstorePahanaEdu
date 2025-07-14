package dao;

import model.Staff;
import util.DAOExeption;
import util.ValidationException;

import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffDAOImpl implements StaffDAO {

    private static final Logger logger = Logger.getLogger(StaffDAOImpl.class.getName());

    private static final String TABLE = "staff";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_CONTACT_NUMBER = "contact_number";
    private static final String COL_PASSWORD_HASH = "password_hash";
    private static final String COL_IS_VERIFIED = "is_verified";

    private final Connection connection;

    public StaffDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Staff staff) throws DAOExeption {
        String sql = "INSERT INTO " + TABLE + " (" + COL_ID + ", " + COL_USERNAME + ", " + COL_FIRST_NAME + ", "
                + COL_LAST_NAME + ", " + COL_EMAIL + ", " + COL_CONTACT_NUMBER + ", " + COL_PASSWORD_HASH + ", " + COL_IS_VERIFIED + ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            if (e.getMessage().contains(TABLE + "." + COL_USERNAME)) {
                throw new ValidationException("Username already exists.");
            } else if (e.getMessage().contains(TABLE + "." + COL_EMAIL)) {
                throw new ValidationException("Email already exists.");
            } else {
                throw new ValidationException("Duplicate entry detected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while saving staff", e);
            throw new DAOExeption("Database error while saving staff.", e);
        }
    }

    @Override
    public Optional<Staff> findByEmail(String email) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_EMAIL + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractStaffFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding staff by email", e);
            throw new DAOExeption("Error finding staff by email.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Staff> findByUsername(String username) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_USERNAME + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractStaffFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding staff by username", e);
            throw new DAOExeption("Error finding staff by username.", e);
        }
        return Optional.empty();
    }

    @Override
    public int countStaff() throws DAOExeption {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error counting staff", e);
            throw new DAOExeption("Error counting staff.", e);
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
            logger.log(Level.SEVERE, "Error verifying staff", e);
            throw new DAOExeption("Error verifying staff.", e);
        }
    }

    @Override
    public int getMaxStaffIdNumber() throws DAOExeption {
        String prefix = "st__";
        String sql = "SELECT MAX(CAST(SUBSTRING(" + COL_ID + ", LENGTH(?) + 1) AS UNSIGNED)) AS max_id FROM "
                + TABLE + " WHERE " + COL_ID + " LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, prefix);
            stmt.setString(2, prefix + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_id");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting max staff ID number", e);
            throw new DAOExeption("Error getting max staff ID number.", e);
        }
        return 0;
    }

    @Override
    public Optional<Staff> findById(String id) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_ID + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractStaffFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding staff by id", e);
            throw new DAOExeption("Error finding staff by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Staff> findByUsernameOrEmail(String input) throws DAOExeption {
        String sql = "SELECT * FROM " + TABLE + " WHERE " + COL_USERNAME + " = ? OR " + COL_EMAIL + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, input);
            ps.setString(2, input);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractStaffFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding staff by username or email", e);
            throw new DAOExeption("Error finding staff by username or email.", e);
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
            logger.log(Level.SEVERE, "Error updating staff password", e);
            throw new DAOExeption("Error updating staff password.", e);
        }
    }

    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getString(COL_ID));
        staff.setUsername(rs.getString(COL_USERNAME));
        staff.setFirstName(rs.getString(COL_FIRST_NAME));
        staff.setLastName(rs.getString(COL_LAST_NAME));
        staff.setEmail(rs.getString(COL_EMAIL));
        staff.setContactNumber(rs.getString(COL_CONTACT_NUMBER));
        staff.setPasswordHash(rs.getString(COL_PASSWORD_HASH));
        staff.setVerified(rs.getBoolean(COL_IS_VERIFIED));
        return staff;
    }
}
