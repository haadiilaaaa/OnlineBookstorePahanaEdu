package controller.customer;

import dao.OrderDAO;
import dao.OrderDAOImpl;
import dao.OrderItemDAO;
import dao.OrderItemDAOImpl;
import db.DBConnection;
import service.customer.CancelOrderService;
import service.customer.CancelOrderServiceImpl;

import javax.servlet.ServletException;

import javax.servlet.http.*;
import java.io.IOException;   
import java.sql.Connection;


public class CancelOrderServlet extends HttpServlet {
    
    
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.sendRedirect("CustomerOrderHistory");
}


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderId = request.getParameter("orderId");

       try (Connection conn = DBConnection.getInstance().getConnection()) {
    OrderItemDAO orderItemDAO = new OrderItemDAOImpl(conn); // ✅ Create the required dependency
    OrderDAOImpl orderDAO = new OrderDAOImpl(conn, orderItemDAO); // ✅ Pass both
            CancelOrderService cancelService = new CancelOrderServiceImpl(orderDAO);
            cancelService.cancelOrder(orderId);

            response.sendRedirect("CustomerOrderHistoryServlet"); // redirect to history page
        }catch (Exception e) {
    e.printStackTrace(); // check your server logs for full trace
    System.err.println("Order ID causing error: " + orderId);
    response.sendError(500, "Unable to cancel order");
}
    }
}
