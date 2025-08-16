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

public class EditCustomerServlet extends HttpServlet {

    private CustomerManagemetService customerService;

    private Connection conn; // keep reference for closing

    @Override
public void init() {
    try {
        conn = DBConnection.getInstance().getConnection();
        this.customerService = new CustomerManagementServiceImpl(new CustomerDAOimpl(conn));
    } catch (Exception e) {
        e.printStackTrace(); // You can replace this with a proper logger if needed
        throw new RuntimeException("Failed to initialize AdminManageUsersServlet", e);
    }
}
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String customerId = req.getParameter("customerId");
        if (customerId == null || customerId.isEmpty()) {
            resp.sendRedirect("AdminManageUsersServlet"); // redirect if no ID provided
            return;
        }
        try {
            CustomerDTO customer = customerService.getCustomerById(customerId);
            if (customer == null) {
                resp.sendRedirect("AdminManageUsersServlet"); // customer not found
                return;
            }
            req.setAttribute("customer", customer);
            req.getRequestDispatcher("/admin/edit_customer.jsp").forward(req, resp);
        } catch (DAOExeption e) {
            throw new ServletException("Error loading customer for edit", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String contactNumber = req.getParameter("contactNumber");
        String address = req.getParameter("address");

        CustomerDTO customer = new CustomerDTO();
        customer.setId(id);
        customer.setUsername(username);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setContactNumber(contactNumber);
        customer.setAddress(address);

        try {
            boolean updated = customerService.updateCustomer(customer);
          HttpSession session = req.getSession();
if (updated) {
    session.setAttribute("successMessage", "Customer updated successfully!");
    resp.sendRedirect("AdminManageUsersServlet");
} else {
    req.setAttribute("errorMessage", "Failed to update customer");
    req.setAttribute("customer", customer);
    req.getRequestDispatcher("/WEB-INF/admin/edit_customer.jsp").forward(req, resp);
}

        } catch (DAOExeption e) {
            req.setAttribute("error", "Error updating customer: " + e.getMessage());
            req.setAttribute("customer", customer);
            req.getRequestDispatcher("/WEB-INF/admin/edit_customer.jsp").forward(req, resp);
        }
    }
}
