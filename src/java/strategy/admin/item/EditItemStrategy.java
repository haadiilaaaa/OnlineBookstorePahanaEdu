package strategy.admin.item;

import dto.ItemDTO;
import service.admin.ItemService;
import dao.CategoryDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditItemStrategy implements ItemActionStrategy {

    private final ItemService itemService;
    private final CategoryDAO categoryDAO;

    public EditItemStrategy(ItemService itemService, CategoryDAO categoryDAO) {
        this.itemService = itemService;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public StrategyResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ItemDTO dto = util.ItemRequestPasser.parseFromRequest(req);

        System.out.println("[EDIT STRATEGY] categoryId = " + dto.getCategoryId());

        if (dto.getCategoryId() == null || dto.getCategoryId().isEmpty()) {
            throw new IllegalArgumentException("Invalid category ID: null");
        }

        itemService.updateItem(dto);

        // Set success message as request attribute before redirect (won't survive redirect)
        // Ideally use session or flash scope. For now, just logging or consider improvement.
        req.getSession().setAttribute("success", "Item updated successfully.");

        System.out.println("EditItemStrategy received ID: " + req.getParameter("id"));

        // Redirect to servlet to reload list and avoid form resubmission
        return new StrategyResult("AddItemServlet", true);
    }
}
