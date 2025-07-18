package dao;

import db.DBConnection;
import model.Guideline;
import util.DAOExeption;

import java.sql.*;
import java.util.*;

public class GuidelineDAOImpl implements GuidelineDAO {
    private final Connection conn;

    public GuidelineDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Guideline guideline) throws DAOExeption {
        String sql = "INSERT INTO guidelines (id, title, content) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guideline.getId());
            stmt.setString(2, guideline.getTitle());
            stmt.setString(3, guideline.getContent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExeption("Error saving guideline", e);
        }
    }

    @Override
    public void update(Guideline guideline) throws DAOExeption {
        String sql = "UPDATE guidelines SET title = ?, content = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "DELETE FROM guidelines WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExeption("Error deleting guideline", e);
        }
    }

    @Override
    public List<Guideline> findAll() throws DAOExeption {
        List<Guideline> list = new ArrayList<>();
        String sql = "SELECT * FROM guidelines ORDER BY updated_at DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Guideline g = new Guideline();
                g.setId(rs.getString("id"));
                g.setTitle(rs.getString("title"));
                g.setContent(rs.getString("content"));
                list.add(g);
            }
        } catch (SQLException e) {
            throw new DAOExeption("Error fetching guidelines", e);
        }
        return list;
    }

    @Override
    public Guideline findById(String id) throws DAOExeption {
        String sql = "SELECT * FROM guidelines WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Guideline g = new Guideline();
                    g.setId(rs.getString("id"));
                    g.setTitle(rs.getString("title"));
                    g.setContent(rs.getString("content"));
                    return g;
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
    String sql = "SELECT id FROM guidelines ORDER BY id ASC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
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
