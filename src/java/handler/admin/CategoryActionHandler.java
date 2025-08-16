package handler.admin;

import command.admin.category.*;
import dto.CategoryDTO;
import service.admin.CategoryService;
import util.IDGenerator;
import util.NextSequentialIDGenerator; // Import the correct class
import util.contannts.*;
import util.enums.CommandResultType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
                    // Create the list of existing IDs
                    List<String> existingIds = categoryService.getAllCategories().stream()
                            .map(CategoryDTO::getId)
                            .collect(Collectors.toList());

                    // Instantiate the correct IDGenerator with the prefix and existing IDs
                    IDGenerator<String> categoryIdGenerator = new NextSequentialIDGenerator("cat", existingIds);
                    
                    // Pass both the service and the IDGenerator to the constructor
                    command = new AddCategoryCommand(categoryService, categoryIdGenerator);
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