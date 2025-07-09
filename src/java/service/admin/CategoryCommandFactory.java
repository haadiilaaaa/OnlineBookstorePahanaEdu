package service.admin;

import service.admin.*;
import command.admin.category.*;
import service.admin.CategoryService;

import java.util.HashMap;
import java.util.Map;

public class CategoryCommandFactory {

    private final Map<String, CategoryActionCommand> commandMap = new HashMap<>();
    private final CategoryService categoryService;

    public CategoryCommandFactory(CategoryService categoryService) {
        this.categoryService = categoryService;

       
        commandMap.put("add", new AddCategoryCommand(categoryService));
        commandMap.put("edit", new EditCategoryCommand(categoryService));
        commandMap.put("delete", new DeleteCategoryCommand(categoryService));
        
    }

    public CategoryActionCommand getCommand(String action) {
        return commandMap.getOrDefault(action.toLowerCase(), commandMap.get("add"));
    }

    public CategoryService getService() {
        return categoryService;
    }
}
