package controller.admin;

import dto.CategoryDTO;
import dto.ItemDTO;
import dto.ItemSearchCriteria;
import dto.UserSession;
import service.admin.CategoryService;
import service.admin.ItemService;
import strategy.admin.item.ItemActionStrategy;
import strategy.admin.item.StrategyResult;
import util.ItemSearchParser;
import strategy.admin.item.ItemStrategyRegistrar;
import service.admin.AddItemServiceManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@MultipartConfig
public class AddItemServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddItemServlet.class.getName());

    private static final String ACTION_EDIT = "edit";
    private static final String ACTION_DEFAULT = "default";

    private static final String USER_SESSION_KEY = "user"; // ✅ must match login servlet
    private static final String REQUIRED_ROLE = "admin";

    private ItemService itemService;
    private CategoryService categoryService;
    private Map<String, ItemActionStrategy> strategyMap;

    @Override
    public void init() throws ServletException {
        try {
            this.itemService = AddItemServiceManager.get(ItemService.class);
            this.categoryService = AddItemServiceManager.get(CategoryService.class);
            this.strategyMap = ItemStrategyRegistrar.registerAll(itemService, categoryService);

            LOGGER.info("[INIT] Services loaded and strategies registered.");
        } catch (Exception e) {
            LOGGER.severe("[INIT FAILED] " + e.getMessage());
            throw new ServletException("Initialization failed in AddItemServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAuthorized(req, resp)) return;

        String action = req.getParameter("action");

        if (ACTION_EDIT.equals(action)) {
            try {
                ItemActionStrategy strategy = strategyMap.get(ACTION_EDIT);
                StrategyResult result = strategy.execute(req, resp);

                if (!resp.isCommitted()) {
                    navigate(result, req, resp);
                }
            } catch (Exception e) {
                LOGGER.warning("[GET][EDIT] Error: " + e.getMessage());
                req.setAttribute("error", "Failed to load edit form: " + e.getMessage());
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            }
            return;
        }

        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            req.setAttribute("categories", categories);

            ItemSearchCriteria criteria = ItemSearchParser.parse(req);
            List<ItemDTO> items = itemService.searchItems(
                    criteria.getKeyword(),
                    criteria.getCategoryId(),
                    criteria.getMinPrice(),
                    criteria.getMaxPrice()
            );

            req.setAttribute("items", items);
            req.getRequestDispatcher("/admin/addItem.jsp").forward(req, resp);

        } catch (Exception e) {
            LOGGER.warning("[GET][LIST] Error: " + e.getMessage());
            req.setAttribute("error", "Failed to load items.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAuthorized(req, resp)) return;

        String action = Optional.ofNullable(req.getParameter("action")).orElse(ACTION_DEFAULT);

        try {
            ItemActionStrategy strategy = strategyMap.getOrDefault(action, strategyMap.get(ACTION_DEFAULT));
            StrategyResult result = strategy.execute(req, resp);

            if (!resp.isCommitted()) {
                navigate(result, req, resp);
            }
        } catch (Exception e) {
            LOGGER.warning("[POST][" + action + "] Error: " + e.getMessage());
            if (!resp.isCommitted()) {
                req.setAttribute("error", "Operation failed: " + e.getMessage());
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            }
        }
    }

    /**
     * Checks if the current session user is authorized (admin).
     */
    private boolean isAuthorized(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            LOGGER.warning("Session is null. Redirecting to login.");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return false;
        }

        UserSession user = (UserSession) session.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            LOGGER.warning("UserSession not found in session.");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return false;
        }

        if (!REQUIRED_ROLE.equalsIgnoreCase(user.getUserType())) {
            LOGGER.warning("Unauthorized access attempt by user: " + user.getEmail());
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return false;
        }

        return true;
    }

    /**
     * Navigate based on StrategyResult (either redirect or forward).
     */
    private void navigate(StrategyResult result, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (result.isRedirect()) {
            resp.sendRedirect(result.getView());
        } else {
            req.getRequestDispatcher(result.getView()).forward(req, resp);
        }
    }
}
