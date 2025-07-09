package command.admin.category;

import service.admin.CategoryService;
import util.contannts.*;
import command.admin.category.CommandResult;
import util.enums.CommandResultType;

import javax.servlet.http.*;

public class DeleteCategoryCommand implements CategoryActionCommand {

    private final CategoryService categoryService;

    public DeleteCategoryCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter(ParameterKeys.ID);

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Category ID is required for deletion.");
        }

        categoryService.deleteCategory(id);
        req.getSession().setAttribute(SessionKeys.SUCCESS_MESSAGE, SuccessMessages.CATEGORY_DELETED);

        return new CommandResult(PagePaths.MANAGE_CATEGORIES_SERVLET, CommandResultType.REDIRECT);
    }
}
