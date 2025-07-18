package controller.customer;

import dto.OrderDTO;
import service.customer.CustomerOrderHistoryService;
import service.customer.CustomerOrderHistoryServiceImpl;
import dao.OrderDAOImpl;
import db.DBConnection;
import dao.*;
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
System.out.println("Finding orders for customer ID: " + customerId);

System.out.println("Customer ID: " + customerId);


       try (Connection conn = DBConnection.getInstance().getConnection()) {
    OrderItemDAO orderItemDAO = new OrderItemDAOImpl(conn); // ✅ Create the required dependency
    OrderDAOImpl orderDAO = new OrderDAOImpl(conn, orderItemDAO); // ✅ Pass both
    CustomerOrderHistoryService historyService = new CustomerOrderHistoryServiceImpl(orderDAO);
    
    List<OrderDTO> orders = historyService.getOrdersByCustomer(customerId);
    request.setAttribute("orders", orders);
    request.getRequestDispatcher("customer/customerOrderHistory.jsp").forward(request, response);
} catch (Exception e) {
    e.printStackTrace();
    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load order history.");
}
    }
}
