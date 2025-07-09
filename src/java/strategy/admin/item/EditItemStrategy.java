package strategy.admin.item;

import dto.ItemDTO;
import service.admin.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditItemStrategy implements ItemActionStrategy {

    private final ItemService itemService;

    public EditItemStrategy(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public StrategyResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ItemDTO dto = util.ItemRequestPasser.parseFromRequest(req);

        System.out.println("[EDIT STRATEGY] categoryId = " + dto.getCategoryId());

        if (dto.getCategoryId() == null || dto.getCategoryId().isEmpty()) {
            throw new IllegalArgumentException("Invalid category ID: null");
        }

        itemService.updateItem(dto);

        req.getSession().setAttribute("success", "Item updated successfully.");

        System.out.println("EditItemStrategy received ID: " + req.getParameter("id"));

        // Redirect to the AddItemServlet (will load updated list)
        return new StrategyResult("AddItemServlet", true);
    }
}
