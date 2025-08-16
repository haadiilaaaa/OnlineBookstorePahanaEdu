package dao;

import model.CartItem;
import db.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAOimpl implements CartItemDAO {

    private final Connection connection;

    public CartItemDAOimpl(Connection connection) {
        this.connection = connection;
    }

   // dao.CartItemDAOimpl.java

@Override
public void save(CartItem cartItem) throws Exception {
    String sql = "INSERT INTO cart_item (id, customer_id, item_id, quantity,price) VALUES (?, ?, ?, ?,?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        System.out.println("DEBUG: Attempting to save cart item with ID: " + cartItem.getId());
        stmt.setString(1, cartItem.getId());
        stmt.setString(2, cartItem.getCustomerId());
        stmt.setString(3, cartItem.getItemId());
        stmt.setInt(4, cartItem.getQuantity());
        stmt.setBigDecimal(5, cartItem.getPrice()); 

        stmt.executeUpdate();
        System.out.println("DEBUG: Successfully saved cart item with ID: " + cartItem.getId());
    }
}

    @Override
public int getMaxCartItemNumber() throws Exception {
    String prefix = "cart";
    String sql = "SELECT MAX(CAST(SUBSTRING(id, LENGTH(?) + 1) AS UNSIGNED)) AS max_id " +
                 "FROM cart_item WHERE id LIKE ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, prefix);
        stmt.setString(2, prefix + "%");
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("max_id");
        }
    }

    return 0; // If no records found
}

   @Override
public List<CartItem> findByCustomerId(String customerId) throws Exception {
    List<CartItem> items = new ArrayList<>();
    String sql = """
        SELECT ci.id, ci.customer_id, ci.item_id, ci.quantity, ci.price AS cart_price,
               i.title AS item_title, i.price AS original_price, i.image_url
        FROM cart_item ci
        JOIN item i ON ci.item_id = i.id
        WHERE ci.customer_id = ?
    """;
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, customerId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            CartItem item = new CartItem(
                rs.getString("id"),
                rs.getString("customer_id"),
                rs.getString("item_id"),
                rs.getInt("quantity")
            );
            item.setItemTitle(rs.getString("item_title"));
            item.setPrice(rs.getBigDecimal("cart_price"));        // Use price from cart_item (discounted)
            item.setOriginalPrice(rs.getBigDecimal("original_price")); // Set original price from item table
            item.setImageUrl(rs.getString("image_url"));
            items.add(item);
        }
    }
    return items;
}


    @Override
    public BigDecimal getCartTotal(String customerId) throws Exception {
        String sql = """
            SELECT SUM(i.price * ci.quantity) AS total
            FROM cart_item ci
            JOIN item i ON ci.item_id = i.id
            WHERE ci.customer_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
    // In CartItemDAOimpl
    
   @Override
public void updateQuantity(String customerId, String itemId, int quantity) throws Exception {
    String sql = "UPDATE cart_item SET quantity = ? WHERE customer_id = ? AND item_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, quantity);
        stmt.setString(2, customerId);
        stmt.setString(3, itemId);
        stmt.executeUpdate();
    }
}
@Override
public void deleteByCustomerAndItem(String customerId, String itemId) throws Exception {
    String sql = "DELETE FROM cart_item WHERE customer_id = ? AND item_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, customerId);
        stmt.setString(2, itemId);
        stmt.executeUpdate();
    }
}


    @Override
    public void deleteCartItemsByUserId(String userId) throws Exception {
        String sql = "DELETE FROM cart_item WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }
  

@Override
    public List<CartItem> findAll() throws Exception {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM cart_item";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem(
                    rs.getString("id"),
                    rs.getString("customer_id"),
                    rs.getString("item_id"),
                    rs.getInt("quantity")
                );
                item.setPrice(rs.getBigDecimal("price"));
                items.add(item);
            }
        }
        return items;
    }

    // In CartItemDAOimpl.java
@Override
public CartItem findByCustomerAndItem(String customerId, String itemId) throws Exception {
    String sql = "SELECT * FROM cart_item WHERE customer_id = ? AND item_id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, customerId);
        ps.setString(2, itemId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return extractCartItemFromResultSet(rs);
            }
        }
    }
    return null; // Return null if the item is not found
}

private CartItem extractCartItemFromResultSet(ResultSet rs) throws SQLException {
    String id = rs.getString("id");
    String customerId = rs.getString("customer_id");
    String itemId = rs.getString("item_id");
    int quantity = rs.getInt("quantity");
    BigDecimal price = rs.getBigDecimal("price");
    
    CartItem cartItem = new CartItem(id, customerId, itemId, quantity);
    cartItem.setPrice(price);
    
    return cartItem;
}
}

  

