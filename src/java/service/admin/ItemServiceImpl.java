package service.admin;

import dao.*;
import dto.ItemDTO;
import mapper.ItemMapper;
import model.Category;
import model.Item;
import service.customer.DiscountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;
import dao.*;
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;
    private final ItemMapper itemMapper;
    private final DiscountService discountService;
   
    

   public ItemServiceImpl(ItemDAO itemDAO,
                       CategoryDAO categoryDAO,
                       ItemMapper itemMapper,
                       DiscountService discountService
                      ) {
    this.itemDAO = itemDAO;
    this.categoryDAO = categoryDAO;
    this.itemMapper = itemMapper;
    this.discountService = discountService;
    
}

   private ItemDTO enrichAndMap(Item item, Map<String, Category> categoryMap) throws Exception {
    Category category = categoryMap.get(item.getCategoryId());
    item.setCategory(category);
    discountService.applyBestDiscount(item);
    return itemMapper.toDTO(item);
}

    @Override
    public void addItem(ItemDTO dto) throws Exception {
        Item item = itemMapper.toItem(dto);
        itemDAO.save(item);
    }

    @Override
    public List<ItemDTO> getAllItems() throws Exception {
        List<Item> items = itemDAO.findAll();
        Set<String> categoryIds = items.stream()
                .map(Item::getCategoryId)
                .collect(Collectors.toSet());

        Map<String, Category> categoryMap = categoryDAO.findByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        return items.stream()
                .map(item -> {
                    try {
                        return enrichAndMap(item, categoryMap);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> getItemsByCategory(String categoryId) throws Exception {
        List<Item> items = itemDAO.findByCategoryId(categoryId);
        Set<String> categoryIds = items.stream()
                .map(Item::getCategoryId)
                .collect(Collectors.toSet());

        Map<String, Category> categoryMap = categoryDAO.findByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        return items.stream()
                .map(item -> {
                    try {
                        return enrichAndMap(item, categoryMap);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO getItemById(String id) throws Exception {
        Item item = itemDAO.findById(id);
        if (item != null) {
            Category category = categoryDAO.findById(item.getCategoryId());
            item.setCategory(category);
            discountService.applyBestDiscount(item);
            return itemMapper.toDTO(item);
        }
        return null;
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
        if (item != null) {
            Category category = categoryDAO.findById(item.getCategoryId());
            item.setCategory(category);
            discountService.applyBestDiscount(item);
            return itemMapper.toDTO(item);
        }
        return null;
    }

    @Override
    public List<ItemDTO> searchItems(String keyword, String categoryId, BigDecimal minPrice, BigDecimal maxPrice) throws Exception {
        List<Item> items = itemDAO.advancedSearch(keyword, categoryId, minPrice, maxPrice);
        Set<String> categoryIds = items.stream()
                .map(Item::getCategoryId)
                .collect(Collectors.toSet());

        Map<String, Category> categoryMap = categoryDAO.findByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        return items.stream()
                .map(item -> {
                    try {
                        return enrichAndMap(item, categoryMap);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
