package controller.admin;

import dao.GuidelineDAOImpl;
import db.DBConnection;
import dto.GuidelineDTO;
import model.Guideline;
import service.admin.AdminGuidelineService;
import service.admin.AdminGuidelineServiceImpl;
import service.admin.GuidelineValidator;
import service.common.Validator;
import util.DAOExeption;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ManageGuidelinesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            // ✅ Provide the required Validator<GuidelineDTO> argument
            Validator<GuidelineDTO> validator = new GuidelineValidator();
            AdminGuidelineService service = new AdminGuidelineServiceImpl(
                new GuidelineDAOImpl(conn),
                validator
            );

            List<Guideline> guidelines = service.getAllGuidelines();
            req.setAttribute("guidelines", guidelines);
            req.getRequestDispatcher("/admin/manage-guidelines.jsp").forward(req, resp);
        } catch (DAOExeption | SQLException e) {
            throw new ServletException("Failed to load guidelines", e);
        }
    }
}
