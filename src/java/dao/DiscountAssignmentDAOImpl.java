
package dao;
import java.util.List;

import model.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class DiscountAssignmentDAOImpl implements DiscountAssignmentDAO {
    private final Connection conn;
    public DiscountAssignmentDAOImpl(Connection conn) { this.conn = conn; }

    public void assignDiscount(DiscountAssignment a) throws Exception {
        String sql = "INSERT INTO discount_assignments (id, discount_id, item_id, category_id, type, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getId());
            stmt.setString(2, a.getDiscountId());
            stmt.setString(3, a.getItemId());
            stmt.setString(4, a.getCategoryId());
            stmt.setString(5, a.getType());
            stmt.executeUpdate();
        }
    }

    @Override
public List<DiscountAssignment> findAssignmentsForItem(String itemId, String categoryId) throws Exception {
    List<DiscountAssignment> assignments = new ArrayList<>();
    String sql = """
        SELECT * FROM discount_assignments 
        WHERE 
            (type = 'ITEM' AND item_id = ?) OR 
            (type = 'CATEGORY' AND category_id = ?) OR 
            (type = 'ALL')
    """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, itemId);
        stmt.setString(2, categoryId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            DiscountAssignment a = new DiscountAssignment();
            a.setId(rs.getString("id"));
            a.setDiscountId(rs.getString("discount_id"));
            a.setItemId(rs.getString("item_id"));
            a.setCategoryId(rs.getString("category_id"));
            a.setType(rs.getString("type"));
            a.setCreatedAt(rs.getTimestamp("created_at"));
            assignments.add(a);
        }
    }
    return assignments;
}
@Override
public List<DiscountAssignment> findAssignmentsByDiscountId(String discountId) throws Exception {
    List<DiscountAssignment> list = new ArrayList<>();
    String sql = "SELECT * FROM discount_assignments WHERE discount_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, discountId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            DiscountAssignment assignment = new DiscountAssignment();
            assignment.setId(rs.getString("id"));
            assignment.setDiscountId(rs.getString("discount_id"));
            assignment.setItemId(rs.getString("item_id"));
            assignment.setCategoryId(rs.getString("category_id"));
            assignment.setType(rs.getString("type"));
            list.add(assignment);
        }
    }
    return list;
}
@Override
public void removeAssignmentById(String id) throws Exception {
    String sql = "DELETE FROM discount_assignments WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, id);
        stmt.executeUpdate();
    }
}



}

