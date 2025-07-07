package dao;

import dao.OrderDAO;
import dto.OrderDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import dto.*;

public class OrderDAOImpl implements OrderDAO {
    private final Connection connection;

    public OrderDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveOrder(OrderDTO order) throws Exception {
      String sql = "INSERT INTO orders (id, customer_id, shipping_address, created_at, total_amount, payment_method, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getUserId());
            stmt.setString(3, order.getShippingAddress());
            stmt.setTimestamp(4, new java.sql.Timestamp(order.getOrderDate().getTime()));
            stmt.setBigDecimal(5, order.getTotalAmount());
            stmt.setString(6, order.getPaymentMethod());
            stmt.setString(7, order.getStatus()); // set as "PENDING" when placing


            stmt.executeUpdate();
        }
    }
    
   @Override
public int getNextOrderNumber() throws Exception {
    String sql = "SELECT COUNT(*) FROM orders";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        rs.next();
        return rs.getInt(1) + 1;
    }
}

@Override
public List<OrderDTO> findOrdersByCustomerId(String customerId) throws Exception {
    List<OrderDTO> orders = new ArrayList<>();

    String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY created_at DESC";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, customerId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            OrderDTO order = new OrderDTO();
            order.setOrderId(rs.getString("id"));
            order.setUserId(rs.getString("customer_id"));
            order.setShippingAddress(rs.getString("shipping_address"));
            order.setOrderDate(rs.getTimestamp("created_at"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setPaymentMethod(rs.getString("payment_method"));
            order.setStatus(rs.getString("status"));

            // Fetch order items
            order.setItems(fetchOrderItems(order.getOrderId()));

            orders.add(order);
        }
    }

    return orders;
}

private List<OrderItemDTO> fetchOrderItems(String orderId) throws Exception {
    List<OrderItemDTO> items = new ArrayList<>();
    String sql = "SELECT oi.*, i.title FROM order_items oi JOIN item i ON oi.item_id = i.id WHERE oi.order_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, orderId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            OrderItemDTO item = new OrderItemDTO();
            item.setOrderItemId(rs.getString("id"));
            item.setOrderId(rs.getString("order_id"));
            item.setItemId(rs.getString("item_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getBigDecimal("price"));
            item.setItemTitle(rs.getString("title"));
            items.add(item);
        }
    }

    return items;
}
@Override
public void updateOrderStatus(String orderId, String status) throws Exception {
    String sql = "UPDATE orders SET status = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, status);
        stmt.setString(2, orderId);
        stmt.executeUpdate();
    }
    
    
}

@Override
public List<OrderDTO> findAllOrdersWithCustomerInfo() throws Exception {
    String sql = "SELECT o.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, c.email " +
                 "FROM orders o JOIN customer c ON o.customer_id = c.id ORDER BY o.created_at DESC";
    List<OrderDTO> orders = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            OrderDTO order = new OrderDTO();
            order.setOrderId(rs.getString("id"));
            order.setUserId(rs.getString("customer_id"));
            order.setCustomerName(rs.getString("customer_name"));
            order.setEmail(rs.getString("email"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setShippingAddress(rs.getString("shipping_address"));
            order.setPaymentMethod(rs.getString("payment_method"));
            order.setOrderDate(rs.getTimestamp("created_at"));
            order.setStatus(rs.getString("status"));

            // Fetch and set order items to avoid null
            order.setItems(fetchOrderItems(order.getOrderId()));

            orders.add(order);
        }
    }
    return orders;
}

@Override
public OrderDTO findOrderWithCustomerById(String orderId) throws Exception {
    String sql = "SELECT o.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, c.email " +
                 "FROM orders o JOIN customer c ON o.customer_id = c.id WHERE o.id = ?";
    OrderDTO order = null;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, orderId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            order = new OrderDTO();
            order.setOrderId(rs.getString("id"));
            order.setUserId(rs.getString("customer_id"));
            order.setCustomerName(rs.getString("customer_name"));
            order.setEmail(rs.getString("email"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setShippingAddress(rs.getString("shipping_address"));
            order.setPaymentMethod(rs.getString("payment_method"));
            order.setOrderDate(rs.getTimestamp("created_at"));
            order.setStatus(rs.getString("status"));
        }
    }
    return order;
}

}



