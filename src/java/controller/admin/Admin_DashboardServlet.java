package controller.admin;

import db.DBConnection;
import dto.AdminDashboardDTO;
import dto.UserSession;
import service.admin.AdminDashboardServiceFactory;
import service.admin.AdminDashoardService;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

public class Admin_DashboardServlet extends HttpServlet {

    private AdminDashoardService dashboardService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            this.dashboardService = AdminDashboardServiceFactory.createDashboardService(conn);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize AdminDashboardServlet", e);
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

        UserSession user = (UserSession) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            AdminDashboardDTO dashboardData = dashboardService.loadDashboard(user.getId());

            if (dashboardData == null) {
                request.setAttribute("error", "Dashboard data is not available.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("dashboardData", dashboardData);
            request.getRequestDispatcher("/admin/adminDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to load dashboard.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
