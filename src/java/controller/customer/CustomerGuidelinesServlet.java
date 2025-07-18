package controller.customer;

import dao.GuidelineDAOImpl;
import db.DBConnection;
import model.Guideline;
import util.DAOExeption;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerGuidelinesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            List<Guideline> guidelines = new GuidelineDAOImpl(conn).findAll();
            req.setAttribute("guidelines", guidelines);
            req.getRequestDispatcher("/customer/guidelines.jsp").forward(req, resp);
        } catch (DAOExeption | SQLException e) {
            throw new ServletException("Failed to load guidelines", e);
        }
    }
}
