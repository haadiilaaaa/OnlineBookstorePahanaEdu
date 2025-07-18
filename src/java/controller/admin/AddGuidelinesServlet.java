package controller.admin;

import model.Guideline;
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

public class AddGuidelinesServlet extends HttpServlet {

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String title = request.getParameter("title");
    String content = request.getParameter("content");

    if (title != null && !title.trim().isEmpty() &&
        content != null && !content.trim().isEmpty()) {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            AdminGuidelineService guidelineService = new AdminGuidelineServiceImpl(new GuidelineDAOImpl(conn));
            guidelineService.createGuideline(title.trim(), content.trim());
        } catch (DAOExeption | SQLException e) {
            throw new ServletException("Failed to add guideline", e);
        }
    }

    response.sendRedirect("ManageGuidelinesServlet");
}


}
