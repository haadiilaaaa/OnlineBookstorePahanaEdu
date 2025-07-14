package controller.admin;

import dto.ItemDTO;
import handler.admin.item.CategoryLoader;
import handler.admin.item.ItemSearchHandler;
import service.admin.CategoryService;
import service.admin.ItemService;
import strategy.admin.item.ItemActionStrategy;
import strategy.admin.item.ItemStrategyExcecutor;
import strategy.admin.item.StrategyResult;
import util.contannts.ParameterKeys;
import util.contannts.PagePaths;
import util.enums.ItemAction;
import util.ErrorNavigator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@MultipartConfig
public class AddItemServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddItemServlet.class.getName());

    private ItemStrategyExcecutor strategyExecutor;
    private ItemSearchHandler searchHandler;
    private CategoryLoader categoryLoader;

    @Override
    public void init() throws ServletException {
        ItemService itemService = (ItemService) getServletContext().getAttribute("ItemService");
        CategoryService categoryService = (CategoryService) getServletContext().getAttribute("CategoryService");
        Map<String, ItemActionStrategy> strategyMap = (Map<String, ItemActionStrategy>) getServletContext().getAttribute("ItemStrategyMap");

        if (itemService == null || categoryService == null || strategyMap == null) {
            throw new ServletException("Required services or strategies not initialized");
        }

        this.strategyExecutor = new ItemStrategyExcecutor(strategyMap);
        this.searchHandler = new ItemSearchHandler(itemService);
        this.categoryLoader = new CategoryLoader(categoryService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            ItemAction action = ItemAction.from(req.getParameter(ParameterKeys.ACTION));
            if (action == ItemAction.EDIT) {
                StrategyResult result = strategyExecutor.execute(action, req, resp);
                if (!resp.isCommitted()) navigate(result, req, resp);
                return;
            }

            categoryLoader.load(req);
            searchHandler.handleSearch(req);
            req.getRequestDispatcher(PagePaths.ADMIN_ADD_ITEM_PAGE).forward(req, resp);

        } catch (Exception e) {
            ErrorNavigator.forwardToError("[GET]", e, req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            ItemAction action = ItemAction.from(req.getParameter(ParameterKeys.ACTION));
            System.out.println("DEBUG - Action param: " + req.getParameter(ParameterKeys.ACTION));

            StrategyResult result = strategyExecutor.execute(action, req, resp);
            if (!resp.isCommitted()) navigate(result, req, resp);
        } catch (Exception e) {
            ErrorNavigator.forwardToError("[POST]", e, req, resp);
        }
    }

    private void navigate(StrategyResult result, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (result.isRedirect()) {
            resp.sendRedirect(result.getView());
        } else {
            req.getRequestDispatcher(result.getView()).forward(req, resp);
        }
    }
}
