package controller.admin;

import service.admin.CustomerManagemetService;
import service.admin.CustomerManagementServiceImpl;
import dao.CustomerDAOimpl;
import db.DBConnection;
import util.DAOExeption;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

public class DeleteCustomerServlet extends HttpServlet {

    private CustomerManagemetService customerManagemetService;
      private Connection conn;

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
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String customerId = req.getParameter("customerId");
    HttpSession session = req.getSession();

    try {
        customerManagemetService.deleteCustomerById(customerId);
        session.setAttribute("successMessage", "Customer deleted successfully!");
    } catch (DAOExeption e) {
        session.setAttribute("errorMessage", "Failed to delete customer: " + e.getMessage());
    }

    resp.sendRedirect("manage_users"); // redirect to refresh the list
}

}
