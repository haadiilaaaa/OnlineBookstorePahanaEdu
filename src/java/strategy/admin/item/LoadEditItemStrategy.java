package strategy.admin.item;

import dto.CategoryDTO;
import dto.ItemDTO;
import service.admin.CategoryService;
import service.admin.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LoadEditItemStrategy implements ItemActionStrategy {

    private final ItemService itemService;
    private final CategoryService categoryService;

    public LoadEditItemStrategy(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @Override
    public StrategyResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            req.setAttribute("error", "Invalid item ID for editing.");
            // Forward to main JSP with error
            return new StrategyResult("/admin/addItem.jsp", false);
        }

        // Fetch the item by ID
        ItemDTO item = itemService.findById(id);

        if (item == null) {
            req.setAttribute("error", "Item not found with ID: " + id);
            return new StrategyResult("/admin/addItem.jsp", false);
        }

        // Fetch categories for dropdown
        List<CategoryDTO> categories = categoryService.getAllCategories();

        // Fetch all items to show in the table
        List<ItemDTO> items = itemService.getAllItems();

        // Pass data to JSP
        req.setAttribute("editItem", item);          // Pre-fill form
        req.setAttribute("categories", categories);  // For dropdown
        req.setAttribute("items", items);            // For listing

        // Forward to JSP for edit form display
        return new StrategyResult("/admin/addItem.jsp", false);
    }
}
