package controller.admin;

import dao.GuidelineDAOImpl;
import db.DBConnection;
import service.admin.AdminGuidelineService;
import service.admin.AdminGuidelineServiceImpl;
import service.admin.GuidelineValidator;
import util.DAOExeption;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateGuidelineServlet extends HttpServlet {
   @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String id = req.getParameter("id");
    String title = req.getParameter("title");
    String content = req.getParameter("content");

    HttpSession session = req.getSession();

    try (Connection conn = DBConnection.getInstance().getConnection()) {
        AdminGuidelineService service = new AdminGuidelineServiceImpl(
                new GuidelineDAOImpl(conn),
                new GuidelineValidator()
        );

        service.updateGuideline(id, title, content);
        session.setAttribute("successMessage", "Guideline updated successfully!");

    } catch (DAOExeption | SQLException e) {
        session.setAttribute("errorMessage", "Failed to update guideline: " + e.getMessage());
    }

    resp.sendRedirect("ManageGuidelinesServlet");
}

}
