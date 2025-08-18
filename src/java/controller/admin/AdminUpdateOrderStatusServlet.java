package controller.admin;

import util.contannts.ErrorMessages;
import util.contannts.ContextKeys;
import util.contannts.PagePaths;
import service.admin.AdminOrderService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;   

/**
 * Handles admin order status updates and notifications.
 */
public class AdminUpdateOrderStatusServlet extends HttpServlet {

    private AdminOrderService adminOrderService;

    @Override
    public void init() throws ServletException {
        adminOrderService = (AdminOrderService) getServletContext().getAttribute(ContextKeys.ADMIN_ORDER_SERVICE);
        if (adminOrderService == null) {
            System.out.println("AdminOrderService not found in ServletContext.");
            throw new ServletException("AdminOrderService not initialized.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderId = request.getParameter("orderId");
        String newStatus = request.getParameter("newStatus");

        try {
            adminOrderService.updateOrderStatusAndNotify(orderId, newStatus);
            response.sendRedirect(PagePaths.ADMIN_ORDER_HISTORY_SERVLET);
        } catch (Exception e) {
            System.out.println("Failed to update order status for orderId: " + orderId + " - " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessages.ORDER_STATUS_UPDATE_FAILED);
        }
    }
}
