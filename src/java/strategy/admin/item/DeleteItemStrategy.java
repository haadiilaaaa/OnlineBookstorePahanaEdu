package strategy.admin.item;

import service.admin.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteItemStrategy implements ItemActionStrategy {

    private final ItemService itemService;

    public DeleteItemStrategy(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public StrategyResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter("id");

        if (id != null && !id.isEmpty()) {
            itemService.deleteItem(id);
            req.getSession().setAttribute("success", "Item deleted successfully.");

        } else {
            req.setAttribute("error", "Invalid item ID for deletion.");
        }

        // 🔁 Redirect to the servlet to refresh the item list
        return new StrategyResult("AddItemServlet", true);
    }
}
