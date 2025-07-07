package service.admin;

import dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    void addCategory(CategoryDTO dto) throws Exception;
    List<CategoryDTO> getAllCategories() throws Exception;
    CategoryDTO getCategoryById(String id) throws Exception;
 void updateCategory(CategoryDTO dto) throws Exception;
void deleteCategory(String id) throws Exception;
 boolean isNameExists(String name) throws Exception;
 int getLastCategoryIdNumber() throws Exception;




    
}
