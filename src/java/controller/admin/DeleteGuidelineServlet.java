package controller.admin;

import dao.GuidelineDAOImpl;
import db.DBConnection;
import dto.GuidelineDTO;
import service.admin.AdminGuidelineService;
import service.admin.AdminGuidelineServiceImpl;
import service.admin.GuidelineValidator;
import service.common.Validator;
import util.DAOExeption;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteGuidelineServlet extends HttpServlet {

   @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String id = req.getParameter("id");
    HttpSession session = req.getSession();

    if (id != null && !id.trim().isEmpty()) {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            AdminGuidelineService service = new AdminGuidelineServiceImpl(
                    new GuidelineDAOImpl(conn),
                    new GuidelineValidator()
            );
            service.deleteGuideline(id.trim());
            session.setAttribute("successMessage", "Guideline deleted successfully!");
        } catch (DAOExeption | SQLException e) {
            session.setAttribute("errorMessage", "Failed to delete guideline: " + e.getMessage());
        }
    }

    resp.sendRedirect("ManageGuidelinesServlet");
}


}
