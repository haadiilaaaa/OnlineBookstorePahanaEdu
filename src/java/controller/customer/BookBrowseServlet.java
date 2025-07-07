package controller.customer;

import dto.CategoryDTO;
import dto.ItemDTO;
import model.CartItem;
import dto.UserSession;
import service.admin.CategoryService;
import service.admin.CategoryServiceImpl;
import service.admin.ItemService;
import service.admin.ItemServiceImpl;
import dao.*;
import mapper.CategoryMapper;
import mapper.ItemMapper;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookBrowseServlet extends HttpServlet {

    private ItemService itemService;
    private CategoryService categoryService;
    private CartItemDAO cartItemDAO;  // Add this

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = db.DBConnection.getInstance().getConnection();

            itemService = new ItemServiceImpl(
                new ItemDAOImpl(conn),
                new CategoryDAOImpl(conn),
                new ItemMapper(),
                new DicountDAOimpl(conn),
                new DiscountAssignmentDAOImpl(conn)
            );

            categoryService = new CategoryServiceImpl(
                new CategoryDAOImpl(conn),
                new CategoryMapper()
            );

            cartItemDAO = new CartItemDAOimpl(conn);  // Initialize here

        } catch (Exception e) {
            throw new ServletException("Failed to initialize services", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            UserSession user = (UserSession) session.getAttribute("user");

            if (user != null) {
                // Load cart from DB if not present in session
                Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
                if (cart == null) {
                    cart = new HashMap<>();
                    List<CartItem> dbCartItems = cartItemDAO.findByCustomerId(user.getId());  // <-- Implement this DAO method
                    for (CartItem ci : dbCartItems) {
                        cart.put(ci.getItemId(), ci);
                    }
                    session.setAttribute("cart", cart);
                }
            }

            String categoryId = req.getParameter("categoryId");
            String searchKeyword = req.getParameter("search");
            String minPriceStr = req.getParameter("minPrice");
            String maxPriceStr = req.getParameter("maxPrice");

            BigDecimal minPrice = (minPriceStr != null && !minPriceStr.isEmpty()) ? new BigDecimal(minPriceStr) : null;
            BigDecimal maxPrice = (maxPriceStr != null && !maxPriceStr.isEmpty()) ? new BigDecimal(maxPriceStr) : null;

            List<CategoryDTO> categories = categoryService.getAllCategories();
            List<ItemDTO> items = itemService.searchItems(searchKeyword, categoryId, minPrice, maxPrice);

            req.setAttribute("categories", categories);
            req.setAttribute("items", items);
            req.setAttribute("selectedCategory", categoryId);
            req.setAttribute("searchKeyword", searchKeyword);
            req.setAttribute("minPrice", minPriceStr);
            req.setAttribute("maxPrice", maxPriceStr);

            req.getRequestDispatcher("/customer/bookBrowse.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load books.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
