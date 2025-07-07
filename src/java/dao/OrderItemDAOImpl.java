package dao;

import dao.OrderItemDAO;
import dto.OrderItemDTO;

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
    String sql = "SELECT COUNT(*) AS count FROM order_items";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            return rs.getInt("count") + 1;
        }
        return 1;
    }
}



}
