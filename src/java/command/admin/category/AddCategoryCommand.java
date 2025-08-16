package command.admin.category;

import dto.CategoryDTO;
import service.admin.CategoryService;
import util.IDGenerator;
import util.contannts.*;
import util.enums.CommandResultType;

import javax.servlet.http.*;

public class AddCategoryCommand implements CategoryActionCommand {

    private final CategoryService categoryService;
    private final IDGenerator<String> categoryIdGenerator;

    // The constructor must accept IDGenerator as a parameter to initialize the field.
    public AddCategoryCommand(CategoryService categoryService, IDGenerator<String> categoryIdGenerator) {
        this.categoryService = categoryService;
        this.categoryIdGenerator = categoryIdGenerator;
    }

    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String name = req.getParameter(ParameterKeys.NAME);
        String description = req.getParameter(ParameterKeys.DESCRIPTION);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }

        if (categoryService.isNameExists(name)) {
            req.setAttribute(AttributeKeys.ERROR_MESSAGE, "Category already exists.");
            return new CommandResult(PagePaths.MANAGE_CATEGORIES_PAGE, CommandResultType.FORWARD);
        }

        // The ID is generated using the injected instance.
        String newId = categoryIdGenerator.generate();

        CategoryDTO dto = new CategoryDTO();
        dto.setId(newId);
        dto.setName(name);
        dto.setDescription(description);

        categoryService.addCategory(dto);
        req.getSession().setAttribute(SessionKeys.SUCCESS_MESSAGE, SuccessMessages.CATEGORY_ADDED);

        return new CommandResult(PagePaths.MANAGE_CATEGORIES_SERVLET, CommandResultType.REDIRECT);
    }
}