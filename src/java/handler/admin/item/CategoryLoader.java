package handler.admin.item;

import dto.CategoryDTO;
import service.admin.CategoryService;
import util.contannts.AttributeKeys;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CategoryLoader {
    private final CategoryService categoryService;

    public CategoryLoader(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void load(HttpServletRequest req) throws Exception {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        req.setAttribute(AttributeKeys.CATEGORIES, categories);
    }
}

