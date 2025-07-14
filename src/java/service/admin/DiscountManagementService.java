
package service.admin;

import java.util.Map;

import dto.DiscountDTO;
import dto.DiscountAssignmentDTO;
import java.util.List;
import model.Item;
import java.math.BigDecimal;
import java.util.Optional;

public interface DiscountManagementService {
    void createDiscount(DiscountDTO dto) throws Exception;
    void assignDiscount(DiscountAssignmentDTO dto) throws Exception;
    List<DiscountDTO> getAllDiscounts() throws Exception;
    List<DiscountAssignmentDTO> getAssignmentsByDiscountId(String discountId) throws Exception;
    Map<String, List<DiscountAssignmentDTO>> getAssignmentMap() throws Exception;
    void removeAssignment(String assignmentId) throws Exception;
   



      
}