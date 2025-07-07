package service.admin;

import dao.CategoryDAO;
import dto.CategoryDTO;
import mapper.CategoryMapper;
import model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryDAO categoryDAO, CategoryMapper categoryMapper) {
        this.categoryDAO = categoryDAO;
        this.categoryMapper = categoryMapper;
    }

   @Override
public void addCategory(CategoryDTO dto) throws Exception {
    String trimmedName = dto.getName().trim();

    if (categoryDAO.findByNameIgnoreCase(trimmedName) != null) {
        throw new IllegalArgumentException("Category with this name already exists.");
    }

    dto.setName(trimmedName); // Save the trimmed version
    categoryDAO.save(categoryMapper.toCategory(dto));
}


    @Override
    public List<CategoryDTO> getAllCategories() throws Exception {
        return categoryDAO.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(String id) throws Exception {
        Category category = categoryDAO.findById(id);
        return category != null ? categoryMapper.toDTO(category) : null;
    }
    @Override
public void updateCategory(CategoryDTO dto) throws Exception {
    Category category = categoryMapper.toCategory(dto);
    categoryDAO.update(category);
}

@Override
public void deleteCategory(String id) throws Exception {
    categoryDAO.delete(id);
}
@Override
public boolean isNameExists(String name) throws Exception {
    Category existing = categoryDAO.findByNameIgnoreCase(name);
    return existing != null;
}

@Override
public int getLastCategoryIdNumber() throws Exception {
    return categoryDAO.getLastCategoryIdNumber(); // delegate to DAO
}


}
