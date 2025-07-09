package controller.admin;

import command.admin.category.CategoryActionCommand;
import service.admin.CategoryCommandFactory;
import dto.CategoryDTO;
import util.contannts.*;
import util.LoggerUtil;
import command.admin.category.CommandResult;
import util.enums.CommandResultType;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles category management (add/edit/delete/list) with full DIP-compliant command delegation.
 */
public class ManageCategoriesServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerUtil.getLogger(ManageCategoriesServlet.class);
    private CategoryCommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        commandFactory = (CategoryCommandFactory) getServletContext().getAttribute(ContextKeys.CATEGORY_COMMAND_FACTORY);
        if (commandFactory == null) {
            throw new ServletException("CategoryCommandFactory not initialized in ServletContext.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAdminUser(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, ErrorMessages.UNAUTHORIZED_ACCESS);
            return;
        }

        try {
            List<CategoryDTO> categories = commandFactory.getService().getAllCategories();
            req.setAttribute(AttributeKeys.CATEGORIES, categories);
            req.getRequestDispatcher(PagePaths.MANAGE_CATEGORIES_PAGE).forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load categories", e);
            req.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.FAILED_TO_LOAD_CATEGORIES);
            req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAdminUser(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, ErrorMessages.UNAUTHORIZED_ACCESS);
            return;
        }

        String action = req.getParameter(ParameterKeys.ACTION);
        if (action == null) action = "add"; // default

        try {
            CategoryActionCommand command = commandFactory.getCommand(action);
            CommandResult result = command.execute(req, resp);

            // Handle redirection or forwarding based on command result
            if (result.getType() == CommandResultType.REDIRECT) {
                resp.sendRedirect(result.getView());
            } else {
                req.getRequestDispatcher(result.getView()).forward(req, resp);
            }
        } catch (IllegalArgumentException iae) {
            LOGGER.log(Level.WARNING, "Validation error: " + iae.getMessage(), iae);
            req.setAttribute(AttributeKeys.ERROR_MESSAGE, iae.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Category operation failed", e);
            req.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.OPERATION_FAILED);
            doGet(req, resp);
        }
    }

    private boolean isAdminUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        Object roleObj = session.getAttribute(SessionKeys.USER_ROLE);
        System.out.println("USER_ROLE: " + session.getAttribute(SessionKeys.USER_ROLE));
        return roleObj != null && "admin".equals(roleObj.toString());
    }
}
