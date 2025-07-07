
package dao;

import model.*;
import java.util.List;
public interface DiscountAssignmentDAO {
    void assignDiscount(DiscountAssignment assignment) throws Exception;
    List<DiscountAssignment> findAssignmentsForItem(String itemId, String categoryId) throws Exception;
    List<DiscountAssignment> findAssignmentsByDiscountId(String discountId) throws Exception;
    void removeAssignmentById(String id) throws Exception;


}
