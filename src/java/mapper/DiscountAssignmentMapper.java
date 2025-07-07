package mapper;

import dto.DiscountAssignmentDTO;
import model.DiscountAssignment;

public class DiscountAssignmentMapper {

    public static DiscountAssignmentDTO toDTO(DiscountAssignment model) {
        if (model == null) return null;

        DiscountAssignmentDTO dto = new DiscountAssignmentDTO();
        dto.setId(model.getId());
        dto.setDiscountId(model.getDiscountId());
        dto.setItemId(model.getItemId());
        dto.setCategoryId(model.getCategoryId());
        dto.setType(model.getType());
        dto.setCreatedAt(model.getCreatedAt());

        return dto;
    }

    public static DiscountAssignment toModel(DiscountAssignmentDTO dto) {
        if (dto == null) return null;

        DiscountAssignment model = new DiscountAssignment();
        model.setId(dto.getId());
        model.setDiscountId(dto.getDiscountId());
        model.setItemId(dto.getItemId());
        model.setCategoryId(dto.getCategoryId());
        model.setType(dto.getType());
        model.setCreatedAt(dto.getCreatedAt());

        return model;
    }
}
