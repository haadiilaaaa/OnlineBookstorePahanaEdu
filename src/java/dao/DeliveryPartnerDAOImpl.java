package dao;

import model.DeliveryPartner;
import java.util.ArrayList;
import java.util.List;
import dto.DeliveryPartnerDTO;
import java.sql.*;
import java.util.Optional;
import util.DAOExeption;

public class DeliveryPartnerDAOImpl implements DeliveryPartnerDAO, PasswordUpdatabale {

    private final Connection connection;

    public DeliveryPartnerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(DeliveryPartner dp) throws Exception {
        String sql = "INSERT INTO delivery_partners " +
            "(id, username, first_name, last_name, email, contact_number, password_hash, vehicle_number, status, is_verified) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dp.getId());
            stmt.setString(2, dp.getUsername());
            stmt.setString(3, dp.getFirstName());
            stmt.setString(4, dp.getLastName());
            stmt.setString(5, dp.getEmail());
            stmt.setString(6, dp.getContactNumber());
            stmt.setString(7, dp.getPasswordHash());
            stmt.setString(8, dp.getVehicleNumber());
            stmt.setString(9, dp.getStatus());
            stmt.setBoolean(10, dp.isVerified());
            stmt.executeUpdate();
        }
    }

    @Override
    public int countPartners() throws Exception {
        String sql = "SELECT COUNT(*) FROM delivery_partners";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // These methods override GenericUserDAO methods - no throws Exception allowed
    @Override
    public Optional<DeliveryPartner> findByEmail(String email) {
        String sql = "SELECT * FROM delivery_partners WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToDeliveryPartner(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding DeliveryPartner by email", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeliveryPartner> findByUsername(String username) {
        String sql = "SELECT * FROM delivery_partners WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToDeliveryPartner(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding DeliveryPartner by username", e);
        }
        return Optional.empty();
    }

    private DeliveryPartner mapRowToDeliveryPartner(ResultSet rs) throws SQLException {
        DeliveryPartner dp = new DeliveryPartner();
        dp.setId(rs.getString("id"));
        dp.setUsername(rs.getString("username"));
        dp.setFirstName(rs.getString("first_name"));
        dp.setLastName(rs.getString("last_name"));
        dp.setEmail(rs.getString("email"));
        dp.setContactNumber(rs.getString("contact_number"));
        dp.setPasswordHash(rs.getString("password_hash"));
        dp.setVehicleNumber(rs.getString("vehicle_number"));
        dp.setStatus(rs.getString("status"));
        dp.setVerified(rs.getBoolean("is_verified"));
        return dp;
    }

    @Override
    public void verify(String userId) throws Exception {
        String sql = "UPDATE delivery_partners SET is_verified = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new Exception("No delivery partner found with id: " + userId);
            }
        }
    }

    @Override
    public void updatePassword(String userId, String newPasswordHash) throws Exception {
        String sql = "UPDATE delivery_partners SET password_hash = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPasswordHash);
            stmt.setString(2, userId);
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new Exception("No delivery partner found with id: " + userId);
            }
        }
    }
    
    @Override
public Optional<DeliveryPartner> findById(String id) {
    String sql = "SELECT * FROM delivery_partners WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapRowToDeliveryPartner(rs));
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error finding DeliveryPartner by id", e);
    }
    return Optional.empty();
}

   @Override
    public List<DeliveryPartnerDTO> findByStatus(String status) throws Exception {
        String sql = "SELECT id, username, first_name, last_name, email, contact_number, vehicle_number FROM delivery_partners WHERE status = ?";
        List<DeliveryPartnerDTO> pendingList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DeliveryPartnerDTO dto = new DeliveryPartnerDTO();
                    dto.setId(rs.getString("id"));
                    dto.setUsername(rs.getString("username"));
                    dto.setFirstName(rs.getString("first_name"));
                    dto.setLastName(rs.getString("last_name"));
                    dto.setEmail(rs.getString("email"));
                    dto.setContactNumber(rs.getString("contact_number"));
                    dto.setVehicleNumber(rs.getString("vehicle_number"));
                    // password fields not needed for admin view
                    pendingList.add(dto);
                }
            }
        }

        return pendingList;
    }
    
    @Override
    public boolean updateStatus(String userId, String newStatus) throws DAOExeption {
        String sql = "UPDATE delivery_partners SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOExeption("Failed to update delivery partner status", e);
        }
    }
    
    @Override
public Optional<DeliveryPartner> findByUsernameOrEmail(String usernameOrEmail) throws DAOExeption {
    String sql = "SELECT * FROM delivery_partners WHERE username = ? OR email = ? LIMIT 1";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, usernameOrEmail);
        stmt.setString(2, usernameOrEmail);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            DeliveryPartner partner = new DeliveryPartner();
            partner.setId(rs.getString("id"));
            partner.setUsername(rs.getString("username"));
            partner.setEmail(rs.getString("email"));
            partner.setFirstName(rs.getString("first_name"));
            partner.setLastName(rs.getString("last_name"));
            partner.setStatus(rs.getString("status"));
            partner.setPasswordHash(rs.getString("password_hash"));
            // set other fields as needed
            return Optional.of(partner);
        }
        return Optional.empty();
    } catch (SQLException e) {
        throw new DAOExeption("Error finding delivery partner by username or email", e);
    }
}

@Override
public List<DeliveryPartnerDTO> getAllPartners() throws DAOExeption {
    List<DeliveryPartnerDTO> partners = new ArrayList<>();
    String sql = "SELECT id, first_name, last_name, username FROM delivery_partners";

    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            DeliveryPartnerDTO dto = new DeliveryPartnerDTO();
            dto.setId(rs.getString("id"));
            dto.setFirstName(rs.getString("first_name"));
            dto.setLastName(rs.getString("last_name"));
            dto.setUsername(rs.getString("username"));
            partners.add(dto);
        }
    } catch (SQLException e) {
        throw new DAOExeption("Error fetching delivery partners", e);
    }

    return partners;
}
@Override
public void update(DeliveryPartner dp) throws Exception {
    String sql = "UPDATE delivery_partners SET " +
                 "first_name = ?, last_name = ?, email = ?, contact_number = ?, vehicle_number = ? " +
                 "WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, dp.getFirstName());
        stmt.setString(2, dp.getLastName());
        stmt.setString(3, dp.getEmail());
        stmt.setString(4, dp.getContactNumber());
        stmt.setString(5, dp.getVehicleNumber());
        stmt.setString(6, dp.getId()); // where clause
        stmt.executeUpdate();
    }
}




}
