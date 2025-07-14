package service.admin;

import dto.ItemDTO;
import java.util.List;
import java.math.BigDecimal;

/**
 * Service interface for managing items in the admin context.
 */
public interface ItemService {

    /**
     * Adds a new item.
     * @param dto the item data transfer object
     * @throws Exception if any error occurs during adding
     */
    void addItem(ItemDTO dto) throws Exception;

    /**
     * Retrieves all items.
     * @return list of all item DTOs
     * @throws Exception if any error occurs during retrieval
     */
    List<ItemDTO> getAllItems() throws Exception;

    /**
     * Retrieves items by category.
     * @param categoryId the category ID to filter items
     * @return list of items in the given category
     * @throws Exception if any error occurs during retrieval
     */
    List<ItemDTO> getItemsByCategory(String categoryId) throws Exception;

    /**
     * Retrieves an item by its ID.
     * @param id the item ID
     * @return item DTO with the given ID
     * @throws Exception if any error occurs during retrieval
     */
    ItemDTO getItemById(String id) throws Exception;

    /**
     * Counts total items.
     * @return the number of items
     * @throws Exception if any error occurs during counting
     */
    int getItemCount() throws Exception;

    /**
     * Updates an existing item.
     * @param item the item DTO with updated info
     * @throws Exception if any error occurs during updating
     */
    void updateItem(ItemDTO item) throws Exception;

    /**
     * Deletes an item by ID.
     * @param id the item ID to delete
     * @throws Exception if any error occurs during deletion
     */
    void deleteItem(String id) throws Exception;

    /**
     * Finds an item by ID (alias of getItemById).
     * @param id the item ID
     * @return the item DTO
     * @throws Exception if any error occurs
     */
    ItemDTO findById(String id) throws Exception;

    /**
     * Searches items with optional filters.
     * @param keyword search keyword for name or description
     * @param categoryId category filter (optional)
     * @param minPrice minimum price filter (optional)
     * @param maxPrice maximum price filter (optional)
     * @return list of items matching the criteria
     * @throws Exception if any error occurs during search
     */
    List<ItemDTO> searchItems(String keyword, String categoryId, BigDecimal minPrice, BigDecimal maxPrice) throws Exception;
}
