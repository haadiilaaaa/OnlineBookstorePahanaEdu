package dao;

import dto.OrderItemDTO;
import util.DAOExeption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {
    private final Connection connection;

    public OrderItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveOrderItems(List<OrderItemDTO> items) throws DAOExeption {
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
        } catch (SQLException e) {
            throw new DAOExeption("Failed to save order items", e);
        }
    }

    @Override
    public int getNextOrderItemNumber() throws DAOExeption {
        String sql = "SELECT MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)) AS max_id FROM order_items";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return maxId + 1;
            }
            return 1; // No entries yet
        } catch (SQLException e) {
            throw new DAOExeption("Failed to get next order item number", e);
        }
    }

    @Override
    public List<OrderItemDTO> findItemsByOrderId(String orderId) throws DAOExeption {
        List<OrderItemDTO> items = new ArrayList<>();
        String sql = "SELECT id, order_id, item_id, quantity, price FROM order_items WHERE order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
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
        } catch (SQLException e) {
            throw new DAOExeption("Failed to find items by order ID", e);
        }

        return items;
    }
    
    @Override
public void deleteOrderItemsByUserId(String userId) throws DAOExeption {
    String sql = "DELETE FROM order_items WHERE order_id IN (SELECT id FROM orders WHERE customer_id = ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, userId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new DAOExeption("Failed to delete order items by user ID", e);
    }
}
}
