package service.admin;

import dto.ItemDTO;
import java.util.List;
import java.math.BigDecimal;
public interface ItemService {
    void addItem(ItemDTO dto) throws Exception;
    List<ItemDTO> getAllItems() throws Exception;
    List<ItemDTO> getItemsByCategory(String categoryId) throws Exception;
    ItemDTO getItemById(String id) throws Exception;
    int getItemCount() throws Exception;
    void updateItem(ItemDTO item) throws Exception;
    void deleteItem(String id) throws Exception;
    ItemDTO findById(String id) throws Exception;
    List<ItemDTO> searchItems(String keyword, String categoryId, BigDecimal minPrice, BigDecimal maxPrice) throws Exception;


}
