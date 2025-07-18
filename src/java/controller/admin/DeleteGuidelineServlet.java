package controller.admin;

import service.admin.AdminGuidelineService;
import service.admin.AdminGuidelineServiceImpl;
import dao.GuidelineDAOImpl;
import db.DBConnection;
import util.DAOExeption;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteGuidelineServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id != null && !id.trim().isEmpty()) {
            try (Connection conn = DBConnection.getInstance().getConnection()) {
                AdminGuidelineService guidelineService = new AdminGuidelineServiceImpl(new GuidelineDAOImpl(conn));
                guidelineService.deleteGuideline(id.trim());
            } catch (DAOExeption | SQLException e) {
                throw new ServletException("Failed to delete guideline", e);
            }
        }

        resp.sendRedirect("ManageGuidelinesServlet"); // Redirect back to manage page
    }
}
