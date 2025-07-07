package controller.admin;

import dao.OrderDAOImpl;
import db.DBConnection;
import dto.OrderDTO;
import service.admin.AdminOrderService;
import service.admin.AdminOrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;


public class AdminUpdateOrderStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        String orderId = request.getParameter("orderId");
        String newStatus = request.getParameter("newStatus");

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            AdminOrderService orderService = new AdminOrderServiceImpl(new OrderDAOImpl(conn));
            orderService.updateOrderStatusAndNotify(orderId, newStatus);

            response.sendRedirect("AdminOrderHistoryServlet"); // redirect back to admin orders page
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Unable to update order status");
        }
    }
}
