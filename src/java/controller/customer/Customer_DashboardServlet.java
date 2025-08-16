package controller.customer;
import util.csrf.*;
import dto.CustomerDashboardDTO;
import dto.UserSession;
import service.customer.CustomerDashboardService;
import util.contannts.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static util.contannts.ContextKeys.CUSTOMER_DASHBOARD_SERVICE;
import service.customer.CartFacade;
import service.customer.ItemServiceFactory;
import java.sql.Connection;

public class Customer_DashboardServlet extends BaseCustomerServlet {

    private static final Logger LOGGER = Logger.getLogger(Customer_DashboardServlet.class.getName());
    private CustomerDashboardService dashboardService;
    private CartFacade cartFacade;

    @Override
    public void init() throws ServletException {
        // Dependencies should be injected via the ServletContext, not created.
        this.dashboardService = (CustomerDashboardService) getServletContext().getAttribute(CUSTOMER_DASHBOARD_SERVICE);
        this.cartFacade = (CartFacade) getServletContext().getAttribute(ContextKeys.CART_FACADE);
        
        if (dashboardService == null || cartFacade == null) {
            throw new ServletException("Required services not initialized in ServletContext.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            UserSession userSession = getAuthenticatedUser(request, response);
            if (userSession == null) return;

            // Load the cart using the facade
            cartFacade.loadCartForUser(request.getSession(), userSession.getId());

            // Load dashboard data
            CustomerDashboardDTO dto = dashboardService.loadDashboard(userSession.getId());

            request.setAttribute(AttributeKeys.DASHBOARD_DATA, dto);
            request.setAttribute(AttributeKeys.CATEGORIES, dashboardService.getAllCategories());

            // Set filter parameters for the JSP
            request.setAttribute(AttributeKeys.SELECTED_CATEGORY, request.getParameter(ParameterKeys.CATEGORY_ID));
            request.setAttribute(AttributeKeys.SEARCH_KEYWORD, request.getParameter(ParameterKeys.SEARCH));
            request.setAttribute(AttributeKeys.MIN_PRICE, request.getParameter(ParameterKeys.MIN_PRICE));
            request.setAttribute(AttributeKeys.MAX_PRICE, request.getParameter(ParameterKeys.MAX_PRICE));

            // The CSRF token generation is also a business concern
            // and might be better handled by a security service or a utility class.
            // For now, let's keep it here for clarity.
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