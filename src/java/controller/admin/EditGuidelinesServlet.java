package controller.admin;
import util.DAOExeption;
import dao.GuidelineDAOImpl;
import db.DBConnection;
import model.Guideline;
import service.admin.AdminGuidelineServiceImpl;
import service.admin.GuidelineValidator;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class EditGuidelinesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id"); // must be here

        if (id == null || id.isEmpty()) {
            resp.sendRedirect("ManageGuidelinesServlet");
            return;
        }
     try (Connection conn = DBConnection.getInstance().getConnection()) {
    AdminGuidelineServiceImpl service = new AdminGuidelineServiceImpl(
            new GuidelineDAOImpl(conn),
            new GuidelineValidator()
    );

    Guideline guideline = service.getById(id); // ✅ now handled
    req.setAttribute("guideline", guideline);
    req.getRequestDispatcher("/admin/edit-guideline.jsp").forward(req, resp);

} catch (SQLException | DAOExeption e) {  // ✅ catch DAOExeption here
    throw new ServletException("Failed to load guideline for editing", e);
}
     
  


    }
}
