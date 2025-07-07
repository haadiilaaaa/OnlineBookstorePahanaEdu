package controller.admin;

import dto.OrderDTO;
import service.admin.AdminOrderService;
import service.admin.AdminOrderServiceImpl;
import dao.OrderDAOImpl;
import db.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class AdminOrderHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            AdminOrderService adminOrderService = new AdminOrderServiceImpl(new OrderDAOImpl(conn));
            List<OrderDTO> orders = adminOrderService.getAllOrdersWithCustomerInfo();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/admin/AdminOrderHistory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load orders.");
        }
    }
}
