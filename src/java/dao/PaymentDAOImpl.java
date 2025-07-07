package dao;

import model.payment;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PaymentDAOImpl implements PaymentDAO {

    private final Connection conn;

    public PaymentDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(payment payment) throws Exception {
        String sql = "INSERT INTO payment (id, order_id, method, status, amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, payment.getId());
            stmt.setString(2, payment.getOrderId());
            stmt.setString(3, payment.getMethod());
            stmt.setString(4, payment.getStatus());
            stmt.setBigDecimal(5, payment.getAmount());
            stmt.executeUpdate();
        }
    }
}
