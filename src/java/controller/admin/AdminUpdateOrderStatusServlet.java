package controller.admin;

import util.contannts.ErrorMessages;
import util.contannts.ContextKeys;
import util.contannts.PagePaths;
import service.admin.AdminOrderService;
import util.LoggerUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles admin order status updates and notifications.
 */
public class AdminUpdateOrderStatusServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerUtil.getLogger(AdminUpdateOrderStatusServlet.class);

    private AdminOrderService adminOrderService;

    @Override
    public void init() throws ServletException {
        adminOrderService = (AdminOrderService) getServletContext().getAttribute(ContextKeys.ADMIN_ORDER_SERVICE);
        if (adminOrderService == null) {
            LOGGER.severe("AdminOrderService not found in ServletContext.");
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
            LOGGER.log(Level.SEVERE, "Failed to update order status for orderId: " + orderId, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessages.ORDER_STATUS_UPDATE_FAILED);
        }
    }
}
