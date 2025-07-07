package dao;

import db.DBConnection;
import model.Discount;
import util.IDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DicountDAOimpl implements DiscountDAO {
    private final Connection conn;
    public DicountDAOimpl(Connection conn) { this.conn = conn; }

   public void save(Discount discount) throws Exception {
    int lastIdNumber = getMaxDiscountIdNumber();        // Step 1: get highest number
    String newId = IDGenerator.generateId("dis", lastIdNumber + 1); // Step 2: generate new ID
    discount.setId(newId);                              // Step 3: assign it

    String sql = "INSERT INTO discounts1 (id, name, description, discount_percent, start_date, end_date, active, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, discount.getId()); // ✅ now it's always unique
        stmt.setString(2, discount.getName());
        stmt.setString(3, discount.getDescription());
        stmt.setDouble(4, discount.getDiscountPercent());
        stmt.setDate(5, new java.sql.Date(discount.getStartDate().getTime()));
        stmt.setDate(6, new java.sql.Date(discount.getEndDate().getTime()));
        stmt.setBoolean(7, discount.isActive());
        stmt.executeUpdate();
    }
}

    public List<Discount> findAll() throws Exception {
        List<Discount> list = new ArrayList<>();
        String sql = "SELECT * FROM discounts1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Discount d = new Discount();
                d.setId(rs.getString("id"));
                d.setName(rs.getString("name"));
                d.setDescription(rs.getString("description"));
                d.setDiscountPercent(rs.getDouble("discount_percent"));
                d.setStartDate(rs.getDate("start_date"));
                d.setEndDate(rs.getDate("end_date"));
                d.setActive(rs.getBoolean("active"));
                list.add(d);
            }
        }
        return list;
    }

    public Discount findById(String id) throws Exception {
        String sql = "SELECT * FROM discounts1 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Discount d = new Discount();
                d.setId(rs.getString("id"));
                d.setName(rs.getString("name"));
                d.setDescription(rs.getString("description"));
                d.setDiscountPercent(rs.getDouble("discount_percent"));
                d.setStartDate(rs.getDate("start_date"));
                d.setEndDate(rs.getDate("end_date"));
                d.setActive(rs.getBoolean("active"));
                return d;
            }
        }
        return null;
    }

    public int getMaxDiscountIdNumber() throws Exception {
        String sql = "SELECT MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)) AS max_id FROM discounts1 WHERE id LIKE 'dis%'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("max_id");
            }
        }
        return 0;
    }
    @Override
public List<Discount> findActiveDiscounts() throws Exception {
    List<Discount> discounts = new ArrayList<>();
    String sql = "SELECT * FROM discounts1 WHERE active = TRUE AND CURDATE() BETWEEN start_date AND end_date";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Discount discount = new Discount();
            discount.setId(rs.getString("id"));
            discount.setName(rs.getString("name"));
            discount.setDescription(rs.getString("description"));
            discount.setDiscountPercent(rs.getDouble("discount_percent"));
            discount.setStartDate(rs.getDate("start_date"));
            discount.setEndDate(rs.getDate("end_date"));
            discount.setActive(rs.getBoolean("active"));
            discounts.add(discount);
        }
    }

    return discounts;
}

}