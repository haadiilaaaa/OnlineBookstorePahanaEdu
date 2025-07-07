package strategy.admin.item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultItemStrategy implements ItemActionStrategy {

    @Override
    public StrategyResult execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("error", "Invalid action specified.");

        // ⬅️ Forward back to the same form (could be an empty/default state)
        return new StrategyResult("/admin/addItem.jsp", false);
    }
}
