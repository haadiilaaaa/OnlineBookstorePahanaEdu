package dao;

import dao.OrderItemDAO;
import dto.OrderItemDTO;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;
import java.sql.ResultSet;
import java.util.TreeSet;
import java.util.HashSet;
import java.sql.SQLException;

public class OrderItemDAOImpl implements OrderItemDAO {
    private final Connection connection;

    public OrderItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveOrderItems(List<OrderItemDTO> items) throws Exception {
        String sql = "INSERT INTO order_items (id, order_id, item_id, quantity, price) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (OrderItemDTO item : items) {
                stmt.setString(1, item.getOrderItemId());
                stmt.setString(2, item.getOrderId());
                stmt.setString(3, item.getItemId());
                stmt.setInt(4, item.getQuantity());
                stmt.setBigDecimal(5, item.getPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    @Override
public int getNextOrderItemNumber() throws SQLException {
    String sql = "SELECT MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)) AS max_id FROM order_items";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            int maxId = rs.getInt("max_id");
            return maxId + 1;
        }
        return 1; // no entries yet
    }
}

    
    @Override
public List<OrderItemDTO> findItemsByOrderId(String orderId) throws Exception {
    List<OrderItemDTO> items = new ArrayList<>();
    String sql = "SELECT id, order_id, item_id, quantity, price FROM order_items WHERE order_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, orderId);  // Set the orderId parameter
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OrderItemDTO item = new OrderItemDTO();
                item.setOrderItemId(rs.getString("id"));
                item.setOrderId(rs.getString("order_id"));
                item.setItemId(rs.getString("item_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                items.add(item);
            }
        }
    }
    return items;
}

}


