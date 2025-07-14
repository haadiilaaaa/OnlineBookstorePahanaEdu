package dao;

import model.Item;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    private final Connection connection;

    public ItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Item item) throws Exception {
        String sql = "INSERT INTO item (id, title, author, description, price, stock_quantity, image_url, category_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getId());
            stmt.setString(2, item.getTitle());
            stmt.setString(3, item.getAuthor());
            stmt.setString(4, item.getDescription());
            stmt.setBigDecimal(5, item.getPrice());
            stmt.setInt(6, item.getStockQuantity());
            stmt.setString(7, item.getImageUrl());
            stmt.setString(8, item.getCategoryId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Item> findAll() throws Exception {
        String sql = "SELECT * FROM item";
        List<Item> items = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // DELEGATE mapping outside
                Item item = new Item();
                item.setId(rs.getString("id"));
                item.setTitle(rs.getString("title"));
                item.setAuthor(rs.getString("author"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setStockQuantity(rs.getInt("stock_quantity"));
                item.setImageUrl(rs.getString("image_url"));
                item.setCategoryId(rs.getString("category_id"));
                item.setCreatedAt(rs.getTimestamp("created_at"));

                items.add(item);
            }
        }

        return items;
    }

    @Override
    public Item findById(String id) throws Exception {
        String sql = "SELECT * FROM item WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getString("id"));
                    item.setTitle(rs.getString("title"));
                    item.setAuthor(rs.getString("author"));
                    item.setDescription(rs.getString("description"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setCategoryId(rs.getString("category_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));

                    return item;
                }
            }
        }

        return null;
    }

    @Override
    public List<Item> findByCategoryId(String categoryId) throws Exception {
        String sql = "SELECT * FROM item WHERE category_id = ?";
        List<Item> items = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getString("id"));
                    item.setTitle(rs.getString("title"));
                    item.setAuthor(rs.getString("author"));
                    item.setDescription(rs.getString("description"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setCategoryId(rs.getString("category_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    items.add(item);
                }
            }
        }

        return items;
    }

    @Override
    public int getItemCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM item";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public void update(Item item) throws Exception {
        String sql = "UPDATE item SET title = ?, author = ?, description = ?, price = ?, stock_quantity = ?, image_url = ?, category_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getAuthor());
            stmt.setString(3, item.getDescription());
            stmt.setBigDecimal(4, item.getPrice());
            stmt.setInt(5, item.getStockQuantity());
            stmt.setString(6, item.getImageUrl());
            stmt.setString(7, item.getCategoryId());
            stmt.setString(8, item.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws Exception {
        String sql = "DELETE FROM item WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Item> advancedSearch(String keyword, String categoryId, BigDecimal minPrice, BigDecimal maxPrice) throws Exception {
        List<Item> items = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM item WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(title) LIKE ? OR LOWER(author) LIKE ?)");
            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            params.add(likeKeyword);
            params.add(likeKeyword);
        }

        if (categoryId != null && !categoryId.trim().isEmpty()) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getString("id"));
                    item.setTitle(rs.getString("title"));
                    item.setAuthor(rs.getString("author"));
                    item.setDescription(rs.getString("description"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setCategoryId(rs.getString("category_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    items.add(item);
                }
            }
        }

        return items;
    }
}
