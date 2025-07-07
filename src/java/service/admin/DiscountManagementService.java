
package service.admin;

import java.util.Map;

import dto.DiscountDTO;
import dto.DiscountAssignmentDTO;
import java.util.List;

public interface DiscountManagementService {
    void createDiscount(DiscountDTO dto) throws Exception;
    void assignDiscount(DiscountAssignmentDTO dto) throws Exception;
    List<DiscountDTO> getAllDiscounts() throws Exception;
    List<DiscountAssignmentDTO> getAssignmentsByDiscountId(String discountId) throws Exception;
    Map<String, List<DiscountAssignmentDTO>> getAssignmentMap() throws Exception;
    void removeAssignment(String assignmentId) throws Exception;


    
}
