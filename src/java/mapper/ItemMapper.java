package mapper;

import dao.CategoryDAO;
import dao.DiscountAssignmentDAO;
import dao.DiscountDAO;
import dto.ItemDTO;
import model.Category;
import model.Discount;
import model.DiscountAssignment;
import model.Item;

import java.math.BigDecimal;
import java.util.List;

public class ItemMapper {

    public Item toItem(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setTitle(dto.getTitle());
        item.setAuthor(dto.getAuthor());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setStockQuantity(dto.getStockQuantity());
        item.setImageUrl(dto.getImageUrl());
        item.setCategoryId(dto.getCategoryId());
        return item;
    }

    public Item toEntity(ItemDTO dto, Category category) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setTitle(dto.getTitle());
        item.setAuthor(dto.getAuthor());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setStockQuantity(dto.getStockQuantity());
        item.setImageUrl(dto.getImageUrl());
        item.setCategoryId(category.getId());
        item.setCategory(category);
        return item;
    }

    public ItemDTO toDTO(Item item,
                     CategoryDAO categoryDAO,
                     DiscountDAO discountDAO,
                     DiscountAssignmentDAO discountAssignmentDAO) throws Exception {

    ItemDTO dto = new ItemDTO();
    dto.setId(item.getId());
    dto.setTitle(item.getTitle());
    dto.setAuthor(item.getAuthor());
    dto.setDescription(item.getDescription());
    dto.setPrice(item.getPrice()); // original price
    dto.setStockQuantity(item.getStockQuantity());
    dto.setImageUrl(item.getImageUrl());
    dto.setCategoryId(item.getCategoryId());

    // category name
    try {
        Category category = categoryDAO.findById(item.getCategoryId());
        dto.setCategoryName(category != null ? category.getName() : "Unknown");
    } catch (Exception e) {
        dto.setCategoryName("Unknown");
    }

    // 🧠 Check if this item has a discount (direct, category, or ALL)
    List<DiscountAssignment> assignments = discountAssignmentDAO.findAssignmentsForItem(item.getId(), item.getCategoryId());
    for (DiscountAssignment assign : assignments) {
        Discount discount = discountDAO.findById(assign.getDiscountId());
        if (discount != null && discount.isActive()) {
            // apply the first matching discount found
            BigDecimal discountValue = item.getPrice()
                .multiply(BigDecimal.valueOf(discount.getDiscountPercent()))
                .divide(BigDecimal.valueOf(100));
            BigDecimal discountedPrice = item.getPrice().subtract(discountValue);

            dto.setHasDiscount(true);
            dto.setOriginalPrice(item.getPrice()); // new field in DTO
            dto.setDiscountedPrice(discountedPrice); // new field in DTO
            break;
        }
    }

    return dto;
}

}
