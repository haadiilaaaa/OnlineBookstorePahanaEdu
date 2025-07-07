package dao;

import model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.Category;


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
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();
        CategoryDAO categoryDAO = new CategoryDAOImpl(connection); // ✅ Add this

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

            // ✅ Set the full Category object
            Category category = categoryDAO.findById(item.getCategoryId());
            item.setCategory(category);

            items.add(item);
        }
    }
    return items;
}
@Override
public Item findById(String id) throws Exception {
    
    String sql = "SELECT i.*, "
           + "i.price AS original_price, "
           + "COALESCE( "
           + "  CASE "
           + "    WHEN da.type = 'PERCENT' THEN i.price - (i.price * d.discount_percent / 100) "
           + "    WHEN da.type = 'AMOUNT' THEN i.price - d.discount_percent "
           + "    ELSE i.price "
           + "  END, "
           + "  i.price "
           + ") AS discounted_price, "
           + "d.name AS discount_name, "
           + "da.type AS discount_type, "
           + "d.discount_percent "
           + "FROM item i "
           + "LEFT JOIN discount_assignments da ON da.item_id = i.id OR da.category_id = i.category_id OR da.type = 'ALL' "
           + "LEFT JOIN discounts1 d ON d.id = da.discount_id "
           + "WHERE i.id = ? "
           + "ORDER BY "
           + "  CASE "
           + "    WHEN da.item_id = i.id THEN 1 "
           + "    WHEN da.category_id = i.category_id THEN 2 "
           + "    WHEN da.type = 'ALL' THEN 3 "
           + "    ELSE 4 "
           + "  END "
           + "LIMIT 1";

System.out.println("findById SQL: " + sql);

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Item item = new Item();
            // Basic item fields
            item.setId(rs.getString("id"));
            item.setTitle(rs.getString("title"));
            item.setAuthor(rs.getString("author"));
            item.setDescription(rs.getString("description"));
            item.setPrice(rs.getBigDecimal("original_price")); // Original price
            item.setStockQuantity(rs.getInt("stock_quantity"));
            item.setImageUrl(rs.getString("image_url"));
            item.setCategoryId(rs.getString("category_id"));
            item.setCreatedAt(rs.getTimestamp("created_at"));

            BigDecimal originalPrice = rs.getBigDecimal("original_price");
            BigDecimal discountedPrice = rs.getBigDecimal("discounted_price");
            String discountLabel = rs.getString("description");
            String discountType = rs.getString("discount_type");
            BigDecimal discountAmount = rs.getBigDecimal("discount_percent");
            System.out.println("Discount Name: " + rs.getString("discount_name"));
System.out.println("Discount Type: " + rs.getString("discount_type"));
System.out.println("Discount Percent: " + rs.getBigDecimal("discount_percent"));
System.out.println("Original Price: " + originalPrice);
System.out.println("Discounted Price: " + discountedPrice);


            // Set discount info on the item
            if (discountedPrice != null && discountedPrice.compareTo(originalPrice) < 0) {
                item.setHasDiscount(true);
                item.setOriginalPrice(originalPrice);
                item.setDiscountedPrice(discountedPrice);
                item.setDiscountLabel(discountLabel);
                // Optionally store discount details
                item.setDiscountType(discountType);
                item.setDiscountAmount(discountAmount);
            } else {
                item.setHasDiscount(false);
                item.setOriginalPrice(originalPrice);
                item.setDiscountedPrice(originalPrice);
            }

            return item;
        }
    }
    return null;
}

    @Override
    public int getItemCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM item";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    @Override
public List<Item> findByCategoryId(String categoryId) throws Exception {
    List<Item> items = new ArrayList<>();
    String sql = "SELECT * FROM item WHERE category_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, categoryId);
        ResultSet rs = stmt.executeQuery();
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
    return items;
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
            stmt.setString(7, item.getCategory().getId());
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

        ResultSet rs = stmt.executeQuery();
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

    return items;
}

    

    
}
