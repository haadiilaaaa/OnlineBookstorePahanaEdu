package mapper;

import dao.CategoryDAO;
import dao.DiscountAssignmentDAO;
import dao.DiscountDAO;
import dto.ItemDTO;
import model.Category;
import model.Discount;
import model.DiscountAssignment;
import model.Item;
import model.DiscountMetaData;

import java.math.BigDecimal;
import java.util.List;

public class ItemMapper {

   public ItemDTO toDTO(Item item) {
    ItemDTO dto = new ItemDTO();
    dto.setId(item.getId());
    dto.setTitle(item.getTitle());
    dto.setAuthor(item.getAuthor());
    dto.setDescription(item.getDescription());
    dto.setPrice(item.getPrice());
    dto.setStockQuantity(item.getStockQuantity());
    dto.setImageUrl(item.getImageUrl());
    dto.setCategoryId(item.getCategoryId());

    // Set category name if available
    if (item.getCategory() != null) {
        dto.setCategoryName(item.getCategory().getName());
    }

    // Handle discount metadata if available
    if (item.getDiscount() != null) {
        DiscountMetaData meta = item.getDiscount();
        dto.setHasDiscount(meta.isHasDiscount());
        dto.setDiscountedPrice(meta.getDiscountedPrice());
        dto.setDiscountLabel(meta.getDiscountLabel());
        dto.setDiscountType(meta.getDiscountType());
        dto.setDiscountAmount(meta.getDiscountAmount());
    } else {
        dto.setHasDiscount(false);
        dto.setDiscountedPrice(item.getPrice()); // fallback to normal price
    }

    return dto;
}

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
    // If your Item has a Category object and not just categoryId,
    // you'll need to set that too in the service layer separately.

    // Discounts usually managed separately, so skip for entity mapping here.

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
    item.setCategoryId(dto.getCategoryId());
    item.setCategory(category);  // set the full category object here if needed

    return item;
}


}
