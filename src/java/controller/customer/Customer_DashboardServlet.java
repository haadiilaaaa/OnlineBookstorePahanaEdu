package controller.customer;

import dao.*;
import db.DBConnection;
import dto.CustomerDashboardDTO;
import dto.UserSession;
import model.CartItem;
import model.Item;
import service.customer.CustomerDashboardService;
import service.customer.CustomerDashboardServiceImpl;
import service.customer.CustomerDiscountService;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Category;

public class Customer_DashboardServlet extends HttpServlet {

    private CustomerDashboardService dashboardService;
    private CustomerDiscountService discountService;
    private ItemDAO itemDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            CustomerDAO customerDAO = new CustomerDAOimpl(connection);
            CartItemDAO cartItemDAO = new CartItemDAOimpl(connection);
            DiscountDAO discountDAO = new DicountDAOimpl(connection);
            CategoryDAO categoryDAO = new CategoryDAOImpl(connection);
            itemDAO = new ItemDAOImpl(connection);

            dashboardService = new CustomerDashboardServiceImpl(customerDAO, cartItemDAO, discountDAO, categoryDAO);
            discountService = new CustomerDiscountService(new DiscountAssignmentDAOImpl(connection), discountDAO);

        } catch (Exception e) {
            throw new ServletException("Failed to initialize CustomerDashboardServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserSession userSession = (UserSession) session.getAttribute("user");
        if (userSession == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            CustomerDashboardDTO dto = dashboardService.loadDashboard(userSession.getId());

            // Apply discount on each cart item
            if (dto != null && dto.getCartItems() != null) {
                for (CartItem cartItem : dto.getCartItems()) {
                    // Fetch the full item details
                    Item item = itemDAO.findById(cartItem.getItemId());
                    if (item != null) {
                        // Calculate discounted price
                        BigDecimal discountedPrice = discountService.calculateDiscountedPrice(item);
                        cartItem.setPrice(discountedPrice);
                        cartItem.setOriginalPrice(item.getPrice());
                    }
                }
            }

            List<Category> categories = dashboardService.getAllCategories();

            request.setAttribute("dashboardData", dto);
            request.setAttribute("categories", categories);

            // Optional filters
            request.setAttribute("selectedCategory", request.getParameter("categoryId"));
            request.setAttribute("searchKeyword", request.getParameter("search"));
            request.setAttribute("minPrice", request.getParameter("minPrice"));
            request.setAttribute("maxPrice", request.getParameter("maxPrice"));

            if (dto != null && dto.getCartItems() != null) {
                Map<String, CartItem> cartMap = new HashMap<>();
                for (CartItem item : dto.getCartItems()) {
                    cartMap.put(item.getItemId(), item);
                }
                session.setAttribute("cart", cartMap);
            }

            request.getRequestDispatcher("/customer/customerDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load dashboard.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
