package service.admin;

import service.admin.CategoryService;
import command.admin.category.*;
import util.IDGenerator;
import util.NextSequentialIDGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryCommandFactory {

    private final Map<String, CategoryActionCommand> commandMap = new HashMap<>();
    private final CategoryService categoryService;

    public CategoryCommandFactory(CategoryService categoryService) throws Exception {
        this.categoryService = categoryService;

        List<String> existingCategoryIds = categoryService.getAllCategories().stream()
            .map(dto -> dto.getId())
            .collect(Collectors.toList());
        
        IDGenerator<String> categoryIdGenerator = new NextSequentialIDGenerator("cat", existingCategoryIds);
        
        commandMap.put("add", new AddCategoryCommand(categoryService, categoryIdGenerator));
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