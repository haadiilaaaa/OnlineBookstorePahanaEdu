package controller.customer;

import dto.OrderDTO;
import service.customer.CustomerOrderHistoryService;
import service.customer.CustomerOrderHistoryServiceImpl;
import dao.OrderDAOImpl;
import db.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class CustomerOrderHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       HttpSession session = request.getSession(false);
dto.UserSession userSession = (dto.UserSession) session.getAttribute("user");

if (userSession == null) {
    response.sendRedirect("login.jsp"); // or your login page
    return;
}

String customerId = userSession.getId(); // ✅ get the actual ID
System.out.println("Customer ID: " + customerId);


        try (Connection conn = DBConnection.getInstance().getConnection()) {
            CustomerOrderHistoryService historyService = new CustomerOrderHistoryServiceImpl(new OrderDAOImpl(conn));
            List<OrderDTO> orders = historyService.getOrdersByCustomer(customerId);
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("customer/customerOrderHistory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load order history.");
        }
    }
}
