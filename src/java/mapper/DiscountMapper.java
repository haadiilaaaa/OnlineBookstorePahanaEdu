package mapper;

import dto.DiscountDTO;
import model.Discount;

public class DiscountMapper {

    public static DiscountDTO toDTO(Discount model) {
        if (model == null) return null;

        DiscountDTO dto = new DiscountDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setDiscountPercent(model.getDiscountPercent());
        dto.setStartDate(model.getStartDate());
        dto.setEndDate(model.getEndDate());
        dto.setActive(model.isActive());

        return dto;
    }

    public static Discount toModel(DiscountDTO dto) {
        if (dto == null) return null;

        Discount model = new Discount();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setDiscountPercent(dto.getDiscountPercent());
        model.setStartDate(dto.getStartDate());
        model.setEndDate(dto.getEndDate());
        model.setActive(dto.isActive());

        return model;
    }
}
