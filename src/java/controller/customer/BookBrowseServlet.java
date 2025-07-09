package controller.customer;

import dto.CategoryDTO;
import dto.ItemDTO;
import dto.UserSession;
import service.admin.CategoryService;
import service.admin.ItemService;
import service.common.CategoryCache;
import service.customer.CartService;
import handler.customer.BookBrowseHandler;
import service.customer.ItemServiceFactory;
import util.contannts.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookBrowseServlet extends BaseCustomerServlet {

    private static final Logger logger = Logger.getLogger(BookBrowseServlet.class.getName());

    private BookBrowseHandler browseHandler;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = db.DBConnection.getInstance().getConnection();

            ItemService itemService = ItemServiceFactory.createItemService(conn);
            CategoryService categoryService = ItemServiceFactory.createCategoryService(conn);
            cartService = ItemServiceFactory.createCartService(conn);  // set to base class field
            CategoryCache categoryCache = ItemServiceFactory.createCategoryCache(categoryService, getServletContext());

            browseHandler = new BookBrowseHandler(itemService, categoryCache, cartService);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize services", e);
            throw new ServletException("Service initialization failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserSession user = getAuthenticatedUser(req, resp);
            if (user == null) return; // redirected to login

            ensureCartLoaded(req, user.getId());

            browseHandler.handleRequest(req, resp);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to process book browse request", e);
            req.setAttribute(AttributeKeys.ERROR, "Failed to load books.");
            req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
        }
    }
}
