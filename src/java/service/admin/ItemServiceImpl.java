package service.admin;

import dao.ItemDAO;
import dao.CategoryDAO;
import dto.ItemDTO;
import mapper.ItemMapper;
import model.Category;
import model.Item;
import java.math.BigDecimal;
import dao.*;
import java.util.List;
import java.util.stream.Collectors;
import model.*;

public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;
    private final ItemMapper itemMapper;
    private final DiscountDAO discountDAO;
    private final DiscountAssignmentDAO discountAssignmentDAO;
    

    public ItemServiceImpl(ItemDAO itemDAO, CategoryDAO categoryDAO, ItemMapper itemMapper, DiscountDAO discountDAO
    ,DiscountAssignmentDAO discountAssignmentDAO) {
        this.itemDAO = itemDAO;
        this.categoryDAO = categoryDAO;
        this.itemMapper = itemMapper;
        this.discountDAO = discountDAO;
        this.discountAssignmentDAO = discountAssignmentDAO;
    }

    @Override
    public void addItem(ItemDTO dto) throws Exception {
       Item item = itemMapper.toItem(dto); // ✅ matches new method

        itemDAO.save(item);
    }

    @Override
public List<ItemDTO> getAllItems() throws Exception {
    return itemDAO.findAll().stream()
        .map(item -> {
            try {
                return itemMapper.toDTO(item, categoryDAO, discountDAO, discountAssignmentDAO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })
        .collect(Collectors.toList());
}

    @Override
public List<ItemDTO> getItemsByCategory(String categoryId) throws Exception {
    return itemDAO.findByCategoryId(categoryId).stream()
        .map(item -> {
            try {
                return itemMapper.toDTO(item, categoryDAO, discountDAO, discountAssignmentDAO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })
        .collect(Collectors.toList());
}

   @Override
public ItemDTO getItemById(String id) throws Exception {
    Item item = itemDAO.findById(id);
    return item != null
        ? itemMapper.toDTO(item, categoryDAO, discountDAO, discountAssignmentDAO)
        : null;
}

    
    @Override
public int getItemCount() throws Exception {
    return itemDAO.getItemCount();
}

   @Override
public void updateItem(ItemDTO dto) throws Exception {
    Category category = categoryDAO.findById(dto.getCategoryId());

    if (category == null) {
        throw new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId());
    }

    Item item = itemMapper.toEntity(dto, category);
    itemDAO.update(item);
}


    @Override
    public void deleteItem(String id) throws Exception {
        itemDAO.delete(id);
    }

    @Override
public ItemDTO findById(String id) throws Exception {
    Item item = itemDAO.findById(id);
    return item != null ? itemMapper.toDTO(item, categoryDAO,discountDAO, discountAssignmentDAO) : null; // ✅ FIXED
}

    
 @Override
public List<ItemDTO> searchItems(String keyword, String categoryId, BigDecimal minPrice, BigDecimal maxPrice) throws Exception {
    return itemDAO.advancedSearch(keyword, categoryId, minPrice, maxPrice)
        .stream()
        .map(item -> {
            try {
                return itemMapper.toDTO(item, categoryDAO, discountDAO, discountAssignmentDAO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })
        .collect(Collectors.toList());
}
}
