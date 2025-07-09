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

public class Customer_DashboardServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Customer_DashboardServlet.class.getName());
    private CustomerDashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        dashboardService = (CustomerDashboardService) getServletContext().getAttribute(ContextKeys.CUSTOMER_DASHBOARD_SERVICE);
        if (dashboardService == null) {
            throw new ServletException("CustomerDashboardService not initialized in ServletContext.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionKeys.USER) == null) {
            response.sendRedirect(PagePaths.LOGIN_PAGE);
            return;
        }

        UserSession userSession = (UserSession) session.getAttribute(SessionKeys.USER);

        try {
            // Load dashboard data
            CustomerDashboardDTO dto = dashboardService.loadDashboard(userSession.getId());

            // Set dashboard data and category list
            request.setAttribute(AttributeKeys.DASHBOARD_DATA, dto);
            request.setAttribute(AttributeKeys.CATEGORIES, dashboardService.getAllCategories());

            // Set filter parameters
            request.setAttribute(AttributeKeys.SELECTED_CATEGORY, request.getParameter(ParameterKeys.CATEGORY_ID));
            request.setAttribute(AttributeKeys.SEARCH_KEYWORD, request.getParameter(ParameterKeys.SEARCH));
            request.setAttribute(AttributeKeys.MIN_PRICE, request.getParameter(ParameterKeys.MIN_PRICE));
            request.setAttribute(AttributeKeys.MAX_PRICE, request.getParameter(ParameterKeys.MAX_PRICE));

            // Set cart map into session using CartUtil
            Map<String, CartItem> cartMap = CartUtil.convertListToMap(dto.getCartItems());
            session.setAttribute(SessionKeys.CART, cartMap);

            // Generate CSRF token and pass to JSP
            String csrfToken = CSRFTokenUtil.generateToken(session);
            request.setAttribute(AttributeKeys.CSRF_TOKEN, csrfToken);

            request.getRequestDispatcher(PagePaths.CUSTOMER_DASHBOARD_PAGE).forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load customer dashboard", e);
            request.setAttribute("error", ErrorMessages.DASHBOARD_LOAD_FAILED);
            request.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(request, response);
        }
    }
}
