package dao;

import mapper.GuidelineMapper;
import model.Guideline;
import util.DAOExeption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuidelineDAOImpl implements GuidelineDAO {

    private final Connection conn;

    public GuidelineDAOImpl(Connection conn) {
        this.conn = conn;
    }

    private static final String INSERT_SQL = "INSERT INTO guidelines (id, title, content) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE guidelines SET title = ?, content = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM guidelines WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM guidelines ORDER BY updated_at DESC";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM guidelines WHERE id = ?";
    private static final String SELECT_ALL_IDS_SQL = "SELECT id FROM guidelines ORDER BY id ASC";

   @Override
public void save(Guideline guideline) throws DAOExeption {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
        stmt.setString(1, guideline.getId());
        stmt.setString(2, guideline.getTitle());
        stmt.setString(3, guideline.getContent());
        int rows = stmt.executeUpdate();
        System.out.println("Inserted rows: " + rows);
        System.out.println("Guideline ID: " + guideline.getId());
        System.out.println("Title: " + guideline.getTitle());
        System.out.println("Content: " + guideline.getContent());
    } catch (SQLException e) {
        throw new DAOExeption("Error saving guideline", e);
    }
}


    @Override
    public void update(Guideline guideline) throws DAOExeption {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, guideline.getTitle());
            stmt.setString(2, guideline.getContent());
            stmt.setString(3, guideline.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExeption("Error updating guideline", e);
        }
    }

    @Override
    public void delete(String id) throws DAOExeption {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExeption("Error deleting guideline", e);
        }
    }

    @Override
    public List<Guideline> findAll() throws DAOExeption {
        List<Guideline> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(GuidelineMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new DAOExeption("Error fetching guidelines", e);
        }
        return list;
    }

    @Override
    public Guideline findById(String id) throws DAOExeption {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return GuidelineMapper.map(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOExeption("Error finding guideline", e);
        }
        return null;
    }

    @Override
    public List<String> findAllIds() throws DAOExeption {
        List<String> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_IDS_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            throw new DAOExeption("Error fetching guideline IDs", e);
        }
        return ids;
    }
}
