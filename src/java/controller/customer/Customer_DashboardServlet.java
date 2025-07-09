package controller.customer;

import dto.CustomerDashboardDTO;
import dto.UserSession;
import model.CartItem;
import service.customer.CustomerDashboardService;
import util.CartUtil;
import util.csrf.CSRFTokenUtil;
import util.contannts.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.customer.ItemServiceFactory;

public class Customer_DashboardServlet extends BaseCustomerServlet {

    private static final Logger LOGGER = Logger.getLogger(Customer_DashboardServlet.class.getName());
    private CustomerDashboardService dashboardService;

   @Override
public void init() throws ServletException {
    super.init(); // optional, if BaseCustomerServlet has init()

    // Obtain the CustomerDashboardService from ServletContext
    dashboardService = (CustomerDashboardService) getServletContext().getAttribute(ContextKeys.CUSTOMER_DASHBOARD_SERVICE);
    if (dashboardService == null) {
        throw new ServletException("CustomerDashboardService not initialized in ServletContext.");
    }

    // Initialize cartService here too, so base class methods can use it
    try {
        java.sql.Connection conn = db.DBConnection.getInstance().getConnection();
        cartService = ItemServiceFactory.createCartService(conn);
    } catch (Exception e) {
        throw new ServletException("Failed to initialize CartService", e);
    }
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Use base class method to get user and redirect if needed
            UserSession userSession = getAuthenticatedUser(request, response);
            if (userSession == null) return; // redirected to login page

            // Use base class method to ensure cart is loaded
            ensureCartLoaded(request, userSession.getId());

            // Load dashboard data
            CustomerDashboardDTO dto = dashboardService.loadDashboard(userSession.getId());

            // Set dashboard data and category list
            request.setAttribute(AttributeKeys.DASHBOARD_DATA, dto);
            request.setAttribute(AttributeKeys.CATEGORIES, dashboardService.getAllCategories());

            // Set filter parameters (pass through request params)
            request.setAttribute(AttributeKeys.SELECTED_CATEGORY, request.getParameter(ParameterKeys.CATEGORY_ID));
            request.setAttribute(AttributeKeys.SEARCH_KEYWORD, request.getParameter(ParameterKeys.SEARCH));
            request.setAttribute(AttributeKeys.MIN_PRICE, request.getParameter(ParameterKeys.MIN_PRICE));
            request.setAttribute(AttributeKeys.MAX_PRICE, request.getParameter(ParameterKeys.MAX_PRICE));

            // Update cart map in session from dashboard DTO (optional if your base class logic suffices)
            Map<String, CartItem> cartMap = CartUtil.convertListToMap(dto.getCartItems());
            request.getSession().setAttribute(SessionKeys.CART, cartMap);

            // Generate CSRF token and pass to JSP
            String csrfToken = CSRFTokenUtil.generateToken(request.getSession());
            request.setAttribute(AttributeKeys.CSRF_TOKEN, csrfToken);

            request.getRequestDispatcher(PagePaths.CUSTOMER_DASHBOARD_PAGE).forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load customer dashboard", e);
            request.setAttribute("error", ErrorMessages.DASHBOARD_LOAD_FAILED);
            request.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(request, response);
        }
    }
}
