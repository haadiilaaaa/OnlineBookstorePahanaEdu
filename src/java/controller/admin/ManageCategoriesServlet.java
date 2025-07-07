package controller.admin;

import dao.CategoryDAOImpl;
import db.DBConnection;
import dto.CategoryDTO;
import mapper.CategoryMapper;
import service.admin.CategoryService;
import service.admin.CategoryServiceImpl;
import util.IDGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ManageCategoriesServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            categoryService = new CategoryServiceImpl(new CategoryDAOImpl(conn), new CategoryMapper());
        } catch (Exception e) {
            throw new ServletException("Failed to initialize category service", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/admin/manageCategories.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load categories.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

  

 @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String action = req.getParameter("action");
    HttpSession session = req.getSession();

    try {
        if ("edit".equalsIgnoreCase(action)) {
            String id = req.getParameter("id");
            String name = req.getParameter("name");
            String description = req.getParameter("description");

            CategoryDTO dto = new CategoryDTO();
            dto.setId(id);
            dto.setName(name);
            dto.setDescription(description);
            categoryService.updateCategory(dto);

            session.setAttribute("success", "Category updated!");

        } else if ("delete".equalsIgnoreCase(action)) {
            String id = req.getParameter("id");
            categoryService.deleteCategory(id);

            session.setAttribute("success", "Category deleted!");

        } else {
            // Default to add
            String name = req.getParameter("name");
            String description = req.getParameter("description");

            if (categoryService.isNameExists(name)) {
                req.setAttribute("error", "Category name already exists.");
                doGet(req, resp);
                return;
            }

            int lastId = categoryService.getLastCategoryIdNumber(); // use MAX not count!
            String newId = IDGenerator.generateId("cat", lastId + 1); // ✅ Avoid duplicates

            CategoryDTO dto = new CategoryDTO();
            dto.setId(newId);
            dto.setName(name);
            dto.setDescription(description);

            categoryService.addCategory(dto);

            session.setAttribute("success", "Category added!");
        }

        // ✅ Redirect after successful action
        resp.sendRedirect("ManageCategoriesServlet");

    } catch (Exception e) {
        e.printStackTrace();
        req.setAttribute("error", "Operation failed: " + e.getMessage());
        doGet(req, resp);
    }
}

}


