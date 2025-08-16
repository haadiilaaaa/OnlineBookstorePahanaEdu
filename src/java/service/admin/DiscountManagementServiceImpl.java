package service.admin;

import dao.*;
import dto.*;
import mapper.DiscountMapper;
import mapper.DiscountAssignmentMapper;
import model.*;
import util.IDGenerator;
import model.Item;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

public class DiscountManagementServiceImpl implements DiscountManagementService {

    private final DiscountDAO discountDAO;
    private final DiscountAssignmentDAO assignmentDAO;
    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;
    private final IDGenerator<String> discountIdGenerator;
    private final IDGenerator<String> uuidGenerator;

    public DiscountManagementServiceImpl(
        DiscountDAO discountDAO,
        DiscountAssignmentDAO assignmentDAO,
        ItemDAO itemDAO,
        CategoryDAO categoryDAO,
        IDGenerator<String> discountIdGenerator, // New dependency
        IDGenerator<String> uuidGenerator        // New dependency
    ) {
        this.discountDAO = discountDAO;
        this.assignmentDAO = assignmentDAO;
        this.itemDAO = itemDAO;
        this.categoryDAO = categoryDAO;
        this.discountIdGenerator = discountIdGenerator;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public void createDiscount(DiscountDTO dto) throws Exception {
        Discount discount = DiscountMapper.toModel(dto);
        // Use the injected IDGenerator instance
        discount.setId(discountIdGenerator.generate());
        discountDAO.save(discount);
    }

    @Override
    public void assignDiscount(DiscountAssignmentDTO dto) throws Exception {
        DiscountAssignment assignment = DiscountAssignmentMapper.toModel(dto);
        // Use the injected IDGenerator instance
        assignment.setId(uuidGenerator.generate());
        assignmentDAO.assignDiscount(assignment);
    }

    @Override
    public List<DiscountDTO> getAllDiscounts() throws Exception {
        return discountDAO.findAll().stream()
            .map(DiscountMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<DiscountAssignmentDTO> getAssignmentsByDiscountId(String discountId) throws Exception {
        List<DiscountAssignment> assignments = assignmentDAO.findAssignmentsByDiscountId(discountId);
        return assignments.stream().map(assignment -> {
            DiscountAssignmentDTO dto = new DiscountAssignmentDTO();
            dto.setId(assignment.getId());
            dto.setDiscountId(assignment.getDiscountId());
            dto.setType(assignment.getType());
            dto.setItemId(assignment.getItemId());
            dto.setCategoryId(assignment.getCategoryId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DiscountAssignmentDTO>> getAssignmentMap() throws Exception {
        Map<String, List<DiscountAssignmentDTO>> map = new HashMap<>();

        List<DiscountDTO> discounts = discountDAO.findAll().stream()
            .map(DiscountMapper::toDTO)
            .collect(Collectors.toList());

        for (DiscountDTO discount : discounts) {
            List<DiscountAssignment> assignments = assignmentDAO.findAssignmentsByDiscountId(discount.getId());

            List<DiscountAssignmentDTO> assignmentDTOs = new ArrayList<>();
            for (DiscountAssignment assignment : assignments) {
                DiscountAssignmentDTO dto = new DiscountAssignmentDTO();
                dto.setId(assignment.getId());
                dto.setType(assignment.getType());
                dto.setItemId(assignment.getItemId());
                dto.setCategoryId(assignment.getCategoryId());

                // Attach human-readable display name
                if ("ITEM".equals(dto.getType())) {
                    Item item = itemDAO.findById(dto.getItemId());
                    dto.setDisplayName(item != null ? item.getTitle() : dto.getItemId());
                } else if ("CATEGORY".equals(dto.getType())) {
                    Category cat = categoryDAO.findById(dto.getCategoryId());
                    dto.setDisplayName(cat != null ? cat.getName() : dto.getCategoryId());
                } else {
                    dto.setDisplayName("All Items");
                }

                assignmentDTOs.add(dto);
            }

            map.put(discount.getId(), assignmentDTOs);
        }

        return map;
    }
    
    @Override
    public void removeAssignment(String assignmentId) throws Exception {
        assignmentDAO.removeAssignmentById(assignmentId);
    }
}