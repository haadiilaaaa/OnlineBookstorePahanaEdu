package controller.admin;

import dto.AdminDashboardDTO;
import dto.UserSession;
import service.admin.AdminDashoardService;
import util.contannts.AttributeKeys;
import util.contannts.PagePaths;
import util.contannts.RoleConstants;
import util.contannts.SessionKeys;  // Added import for SessionKeys

import javax.servlet.ServletException;
import javax.servlet.http.*;  
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Admin_DashboardServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Admin_DashboardServlet.class.getName());

    private AdminDashoardService dashboardService;

    @Override
    public void init() throws ServletException {
        dashboardService = (AdminDashoardService) getServletContext().getAttribute
        ("AdminDashoardService");System.out.println("AdminDashoardService in servlet context? " + (dashboardService != null));
        if (dashboardService == null) {
            throw new ServletException("AdminDashoardService not found in ServletContext");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            redirectToLogin(req, resp);
            return;
        }

        UserSession user = (UserSession) session.getAttribute(SessionKeys.USER);
        if (user == null || !RoleConstants.ADMIN.equalsIgnoreCase(user.getUserType())) {
            redirectToLogin(req, resp);
            return;
        }

        try {
            AdminDashboardDTO dashboardData = dashboardService.loadDashboard(user.getId());

            if (dashboardData == null) {
                req.setAttribute(AttributeKeys.ERROR, "Dashboard data is not available.");
                req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
                return;
            }

            req.setAttribute(AttributeKeys.DASHBOARD_DATA, dashboardData);
            req.getRequestDispatcher(PagePaths.ADMIN_DASHBOARD_PAGE).forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to load admin dashboard", e);
            req.setAttribute(AttributeKeys.ERROR, "Unable to load dashboard.");
            req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
        }
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + PagePaths.LOGIN_PAGE);
    }
}
