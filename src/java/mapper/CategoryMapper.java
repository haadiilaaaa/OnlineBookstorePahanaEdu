package mapper;

import dto.CategoryDTO;
import model.Category;

import java.sql.Timestamp;

public class CategoryMapper {

    public Category toCategory(CategoryDTO dto) {
        return new Category(
            dto.getId(),
            dto.getName(),
            dto.getDescription(),
            dto.getCreatedAt() != null ? dto.getCreatedAt() : new Timestamp(System.currentTimeMillis())
        );
    }

    public CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }
}
