package dao;

import dao.OrderDAO;
import dto.OrderDTO;
import dto.OrderItemDTO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import dto.*;
import java.util.Optional;
import util.DAOExeption;
import java.sql.SQLException;

public class OrderDAOImpl implements OrderDAO {
    private final Connection connection;
    private final OrderItemDAO orderItemDAO;

    public OrderDAOImpl(Connection connection, OrderItemDAO orderItemDAO) {
    this.connection = connection;
    this.orderItemDAO = orderItemDAO;
}
    @Override
public void saveOrder(OrderDTO order) throws Exception {
    String sql = "INSERT INTO orders (id, customer_id, shipping_address, created_at, total_amount, payment_method, status, delivery_fare) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, order.getOrderId());
        stmt.setString(2, order.getUserId());
        stmt.setString(3, order.getShippingAddress());
        stmt.setTimestamp(4, new java.sql.Timestamp(order.getOrderDate().getTime()));
        stmt.setBigDecimal(5, order.getTotalAmount());
        stmt.setString(6, order.getPaymentMethod());
        stmt.setString(7, order.getStatus());
        stmt.setBigDecimal(8, order.getDeliveryFare());  // ✅ New line added
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
            order.setDeliveryFare(rs.getBigDecimal("delivery_fare"));

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
            order.setDeliveryFare(rs.getBigDecimal("delivery_fare"));
            
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
            order.setDeliveryFare(rs.getBigDecimal("delivery_fare"));

            order.setShippingAddress(rs.getString("shipping_address"));
            order.setPaymentMethod(rs.getString("payment_method"));
            order.setOrderDate(rs.getTimestamp("created_at"));
            order.setStatus(rs.getString("status"));
        }
    }
    return order;
}
@Override
public Optional<OrderDTO> findOrderById(String orderId) throws Exception {
    String sql = "SELECT * FROM orders WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, orderId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrderId(rs.getString("id"));
                order.setUserId(rs.getString("customer_id"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setOrderDate(rs.getTimestamp("created_at"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setDeliveryFare(rs.getBigDecimal("delivery_fare"));

                order.setStatus(rs.getString("status"));
                return Optional.of(order);
            }
        }
    }
    return Optional.empty();
}
@Override
public boolean assignDeliveryPartner(String orderId, String partnerId) throws DAOExeption {
    String sql = "UPDATE orders SET delivery_partner_id = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, partnerId);
        stmt.setString(2, orderId);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        throw new DAOExeption("Error assigning delivery partner", e);
    }
}

@Override
public String getCustomerEmailByOrderId(String orderId) throws DAOExeption {
    String sql = "SELECT c.email FROM orders o JOIN customer c ON o.customer_id = c.id WHERE o.id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, orderId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getString("email");
            else return null;
        }
    } catch (SQLException e) {
        throw new DAOExeption("Error fetching customer email by order ID", e);
    }
}


   @Override
public List<OrderDTO> findOrdersByDeliveryPartner(String partnerId) throws DAOExeption {
    List<OrderDTO> orders = new ArrayList<>();

    String sql = "SELECT o.id AS id, o.customer_id, o.shipping_address, o.created_at, " +
             "o.total_amount, o.payment_method, o.status, o.delivery_partner_id, " +
             "o.delivery_fare, " + // ✅ Add this line
             "c.first_name AS customer_first_name, c.last_name AS customer_last_name, c.email " +
             "FROM orders o " +
             "JOIN customer c ON o.customer_id = c.id " +
             "WHERE o.delivery_partner_id = ? " +
             "ORDER BY o.created_at DESC";


    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, partnerId);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OrderDTO dto = new OrderDTO();
                              // now valid
                dto.setOrderId(rs.getString("id")); // Use alias

                System.out.println("Fetched Order ID: " + dto.getOrderId());
                dto.setUserId(rs.getString("customer_id"));
                dto.setShippingAddress(rs.getString("shipping_address"));
                dto.setOrderDate(rs.getTimestamp("created_at"));
                dto.setTotalAmount(rs.getBigDecimal("total_amount"));
                dto.setDeliveryFare(rs.getBigDecimal("delivery_fare"));

                dto.setPaymentMethod(rs.getString("payment_method"));
                dto.setStatus(rs.getString("status"));
                dto.setCustomerName(rs.getString("customer_first_name") + " " + rs.getString("customer_last_name"));
                dto.setEmail(rs.getString("email"));
                dto.setDeliveryPartnerId(rs.getString("delivery_partner_id"));
                System.out.println("Fetched Delivery Partner ID: " + dto.getDeliveryPartnerId());

                // Load order items
                dto.setItems(orderItemDAO.findItemsByOrderId(dto.getOrderId()));

                orders.add(dto);
            }
        }
    } catch (SQLException e) {
        throw new DAOExeption("Error fetching orders for delivery partner", e);
    }
    
    System.out.println("Orders fetched from DB: " + orders.size());
for (OrderDTO dto : orders) {
    System.out.println("Fetched order: " + dto.getOrderId());
}

    return orders;
}

// In OrderDAOImpl
@Override
public BigDecimal getTotalEarningsByDeliveryPartner(String partnerId) throws DAOExeption {
    String sql = "SELECT COALESCE(SUM(delivery_fare), 0) AS total_earnings FROM orders WHERE delivery_partner_id = ? AND status = 'DELIVERED'";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, partnerId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal("total_earnings");
            }
        }
        return BigDecimal.ZERO;
    } catch (SQLException e) {
        throw new DAOExeption("Error calculating total earnings", e);
    }
}
@Override
public int getTotalDeliveriesByDeliveryPartner(String partnerId) throws Exception {
    String sql = "SELECT COUNT(*) FROM orders WHERE delivery_partner_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, partnerId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
@Override
public int countOrdersByPartnerAndStatus(String partnerId, String status) throws Exception {
    String sql = "SELECT COUNT(*) FROM orders WHERE delivery_partner_id = ? AND status = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, partnerId);
        stmt.setString(2, status);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
    }
    return 0;
}

@Override
public List<OrderDTO> findOrdersByDeliveryPartnerWithStatus(String partnerId, String status) throws Exception {
    List<OrderDTO> orders = new ArrayList<>();
    String sql = "SELECT * FROM orders WHERE delivery_partner_id = ? AND status = ? ORDER BY created_at DESC";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, partnerId);
        stmt.setString(2, status);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrderId(rs.getString("id"));
                // set other fields as needed
                orders.add(order);
            }
        }
    }
    return orders;
}
 @Override
    public List<OrderDTO> findAll() throws DAOExeption {
        List<OrderDTO> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders"; 
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                orders.add(mapRowToOrderDTO(rs));
            }
        } catch (SQLException e) {
            throw new DAOExeption("Failed to retrieve all orders", e);
        }
        return orders;
    }

    /**
     * Helper method to map a ResultSet row to an OrderDTO object.
     * This method is crucial for converting database data into a DTO.
     * @param rs The ResultSet containing the order data.
     * @return An OrderDTO object populated with data from the ResultSet.
     * @throws SQLException if a database access error occurs.
     */
    // Corrected mapRowToOrderDTO method within OrderDAOImpl
private OrderDTO mapRowToOrderDTO(ResultSet rs) throws SQLException {
    OrderDTO order = new OrderDTO();
    order.setOrderId(rs.getString("id"));
    
    // Corrected line: use setUserId() instead of setCustomerId()
    order.setUserId(rs.getString("customer_id")); 
    
    order.setOrderDate(rs.getTimestamp("created_at"));
    order.setTotalAmount(rs.getBigDecimal("total_amount"));
    order.setStatus(rs.getString("status"));
    order.setShippingAddress(rs.getString("shipping_address"));
    order.setDeliveryPartnerId(rs.getString("delivery_partner_id"));
    order.setDeliveryFare(rs.getBigDecimal("delivery_fare"));
    
    // Set other OrderDTO properties as needed
    return order;
}

@Override
public void deleteOrdersByUserId(String userId) throws DAOExeption {
    String sql = "DELETE FROM orders WHERE customer_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, userId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new DAOExeption("Failed to delete orders by user ID", e);
    }
}



}



