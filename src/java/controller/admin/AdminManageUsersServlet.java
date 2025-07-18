package controller.admin;

import dto.CustomerDTO;
import service.admin.CustomerManagemetService;
import service.admin.CustomerManagementServiceImpl;
import dao.CustomerDAOimpl;
import db.DBConnection;
import util.DAOExeption;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminManageUsersServlet extends HttpServlet {

    private CustomerManagemetService customerManagemetService;
    private Connection conn; // keep reference for closing

    @Override
public void init() {
    try {
        conn = DBConnection.getInstance().getConnection();
        this.customerManagemetService = new CustomerManagementServiceImpl(new CustomerDAOimpl(conn));
    } catch (Exception e) {
        e.printStackTrace(); // You can replace this with a proper logger if needed
        throw new RuntimeException("Failed to initialize AdminManageUsersServlet", e);
    }
}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<CustomerDTO> customers = customerManagemetService.getAllCustomers();
            req.setAttribute("customers", customers);
            req.getRequestDispatcher("/admin/manage_users.jsp").forward(req, resp);
        } catch (DAOExeption e) {
            throw new ServletException("Failed to fetch customers", e);
        }
    }

    @Override
    public void destroy() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed in AdminManageUsersServlet.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // or use a logger
        }
    }
}
