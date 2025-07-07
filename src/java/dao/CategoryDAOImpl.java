package dao;

import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    private final Connection connection;

    public CategoryDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Category category) throws Exception {
        String sql = "INSERT INTO category (id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Category> findAll() throws Exception {
        String sql = "SELECT * FROM category";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getString("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setCreatedAt(rs.getTimestamp("created_at"));
                categories.add(category);
            }
        }
        return categories;
    }

    @Override
    public Category findById(String id) throws Exception {
        String sql = "SELECT * FROM category WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category category = new Category();
                category.setId(rs.getString("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setCreatedAt(rs.getTimestamp("created_at"));
                return category;
            }
        }
        return null;
    }

    @Override
    public int getCategoryCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM category";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    // In dao/CategoryDAOimpl.java
@Override
public Category findByName(String name) throws Exception {
    String sql = "SELECT * FROM category WHERE name = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Category(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                 rs.getTimestamp("created_at") 
            );
        }
    }
    return null;
}
@Override
public void update(Category category) throws Exception {
    String sql = "UPDATE category SET name = ?, description = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, category.getName());
        stmt.setString(2, category.getDescription());
        stmt.setString(3, category.getId());
        stmt.executeUpdate();
    }
}

@Override
public void delete(String id) throws Exception {
    String sql = "DELETE FROM category WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, id);
        stmt.executeUpdate();
    }
}
@Override
public Category findByNameIgnoreCase(String name) throws Exception {
    String sql = "SELECT * FROM category WHERE LOWER(name) = LOWER(?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return extractCategoryFromResultSet(rs);
        }
        return null;
    }
}

@Override
public void addCategory(Category category) throws Exception {
    String sql = "INSERT INTO category (id, name, description) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, category.getId());
        stmt.setString(2, category.getName());
        stmt.setString(3, category.getDescription());
        stmt.executeUpdate();
    }
}
private Category extractCategoryFromResultSet(ResultSet rs) throws Exception {
    Category category = new Category();
    category.setId(rs.getString("id"));
    category.setName(rs.getString("name"));
    category.setDescription(rs.getString("description"));
    return category;
}

@Override
public int getLastCategoryIdNumber() throws Exception {
    String sql = "SELECT MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)) AS max_id FROM category WHERE id LIKE 'cat%'";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            return rs.getInt("max_id");
        }
    }
    return 0;
}


}
