package handler.admin;

import command.admin.category.*;
import dto.CategoryDTO;
import service.admin.CategoryService;
import util.contannts.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import util.enums.CommandResultType;

public class CategoryActionHandler {

    private final CategoryService categoryService;

    public CategoryActionHandler(CategoryService service) {
        this.categoryService = service;
    }

    public void handleList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            req.setAttribute(AttributeKeys.CATEGORIES, categories);
            req.getRequestDispatcher(PagePaths.MANAGE_CATEGORIES_PAGE).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.FAILED_TO_LOAD_CATEGORIES);
            req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
        }
    }

    public void handleAction(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    String action = req.getParameter(ParameterKeys.ACTION);
    CategoryActionCommand command;

    try {
        switch (action == null ? "" : action.toLowerCase()) {
            case "edit":
                command = new EditCategoryCommand(categoryService);
                break;
            case "delete":
                command = new DeleteCategoryCommand(categoryService);
                break;
            default:
                command = new AddCategoryCommand(categoryService);
                break;
        }

        CommandResult result = command.execute(req, resp);

        if (result.getType() == CommandResultType.REDIRECT) {
            resp.sendRedirect(result.getView());
        } else {
            req.getRequestDispatcher(result.getView()).forward(req, resp);
        }

    } catch (Exception e) {
        req.setAttribute(AttributeKeys.ERROR_MESSAGE, "Category operation failed: " + e.getMessage());
        req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
    }
}

}
