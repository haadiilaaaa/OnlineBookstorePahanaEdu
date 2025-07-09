package command.admin.category;

import dto.CategoryDTO;
import service.admin.CategoryService;
import util.contannts.*;
import command.admin.category.CommandResult;
import util.enums.CommandResultType;

import javax.servlet.http.*;

public class EditCategoryCommand implements CategoryActionCommand {

    private final CategoryService categoryService;

    public EditCategoryCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter(ParameterKeys.ID);
        String name = req.getParameter(ParameterKeys.NAME);
        String description = req.getParameter(ParameterKeys.DESCRIPTION);

        if (id == null || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ID and Name are required for editing.");
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);

        categoryService.updateCategory(dto);
        req.getSession().setAttribute(SessionKeys.SUCCESS_MESSAGE, SuccessMessages.CATEGORY_UPDATED);

        return new CommandResult(PagePaths.MANAGE_CATEGORIES_SERVLET, CommandResultType.REDIRECT);
    }
}
