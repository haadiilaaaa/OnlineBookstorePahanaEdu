
package service.common;


import dto.CategoryDTO;
import service.admin.CategoryService;
import javax.servlet.ServletContext;
import java.util.List;

public class CategoryCache {

    private final CategoryService categoryService;
    private final ServletContext servletContext;
    private static final String CACHE_KEY = "categories";

    public CategoryCache(CategoryService categoryService, ServletContext servletContext) {
        this.categoryService = categoryService;
        this.servletContext = servletContext;
    }

    public List<CategoryDTO> getCategories() throws Exception {
        List<CategoryDTO> categories = (List<CategoryDTO>) servletContext.getAttribute(CACHE_KEY);
        if (categories == null) {
            categories = categoryService.getAllCategories();
            servletContext.setAttribute(CACHE_KEY, categories);
        }
        return categories;
    }
}