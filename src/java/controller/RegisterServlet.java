package controller;

import dto.AdminDTO;
import dto.CustomerDTO;
import dto.StaffDTO;
import strategy.StrategyContext;
import dao.*;
import db.DBConnection;
import util.IDGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.sql.Connection;

public class RegisterServlet extends HttpServlet {

    private StrategyContext strategyContext;
    private CustomerDAO customerDAO;
    private AdminDAO adminDAO;
    private StaffDAO staffDAO;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        strategyContext = (StrategyContext) context.getAttribute("StrategyContext");

        if (strategyContext == null) {
            throw new ServletException("StrategyContext not found in ServletContext.");
        }

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            customerDAO = new CustomerDAOimpl(connection);
            adminDAO = new AminDAOImpl(connection);
            staffDAO = new StaffDAOImpl(connection);
        } catch (Exception e) {
            throw new ServletException("DAO initialization failed.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userType = req.getParameter("userType");
        String userId = null;

        try {
            switch (userType) {
                case "customer" -> {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setUsername(req.getParameter("username"));
                    dto.setFirstName(req.getParameter("first_name"));
                    dto.setLastName(req.getParameter("last_name"));
                    dto.setEmail(req.getParameter("email"));
                    dto.setContactNumber(req.getParameter("contact_number"));
                    dto.setAddress(req.getParameter("address"));
                    dto.setPassword(req.getParameter("password_hash"));

                    String id = IDGenerator.generateId("cus", customerDAO.countCustomers());
                    dto.setId(id); // ✅ set ID
                    strategyContext.executeStrategy("customer", dto);
                    userId = id;
                }
                case "admin" -> {
                    AdminDTO dto = new AdminDTO();
                    dto.setUsername(req.getParameter("username"));
                    dto.setFirstName(req.getParameter("first_name"));
                    dto.setLastName(req.getParameter("last_name"));
                    dto.setEmail(req.getParameter("email"));
                    dto.setContactNumber(req.getParameter("contact_number"));
                    dto.setPassword(req.getParameter("password_hash"));

                    String id = IDGenerator.generateId("admin", adminDAO.countAdmins());
                    dto.setId(id); // ✅ set ID
                    strategyContext.executeStrategy("admin", dto);
                    userId = id;
                }
                case "staff" -> {
                    StaffDTO dto = new StaffDTO();
                    dto.setUsername(req.getParameter("username"));
                    dto.setFirstName(req.getParameter("first_name"));
                    dto.setLastName(req.getParameter("last_name"));
                    dto.setEmail(req.getParameter("email"));
                    dto.setContactNumber(req.getParameter("contact_number"));
                    dto.setPassword(req.getParameter("password_hash"));

                    String id = IDGenerator.generateId("staff", staffDAO.countStaff());
                    dto.setId(id); // ✅ set ID
                    strategyContext.executeStrategy("staff", dto);
                    userId = id;
                }
                default -> throw new IllegalArgumentException("Invalid user type.");
            }

            // ✅ Redirect with actual userId and userType
            resp.sendRedirect("otp-verification.jsp?userId=" + userId + "&userType=" + userType);

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());

String targetPage = switch (userType) {
    case "customer" -> "customerRegister.jsp";
    case "admin" -> "adminRegister.jsp";
    case "staff" -> "staffRegister.jsp";
    default -> "index.jsp"; // fallback
};

req.getRequestDispatcher(targetPage).forward(req, resp);

        }
    }
}
