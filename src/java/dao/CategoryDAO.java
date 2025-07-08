package dao;

import model.Category;
import java.util.List;

public interface CategoryDAO {
    void save(Category category) throws Exception;
    List<Category> findAll() throws Exception;
    Category findById(String id) throws Exception;
    int getCategoryCount() throws Exception;
    Category findByName(String name) throws Exception;
    void update(Category category) throws Exception;
void delete(String id) throws Exception;
 Category findByNameIgnoreCase(String name) throws Exception;
void addCategory (Category category) throws Exception;
int getLastCategoryIdNumber() throws Exception;


    //hi
}
