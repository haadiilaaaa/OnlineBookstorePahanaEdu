package controller.admin;

import util.contannts.AttributeKeys;
import util.contannts.PagePaths;
import util.contannts.ErrorMessages;
import dto.OrderDTO;
import service.admin.AdminOrderService;
import dto.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for displaying order history to the admin user.
 */
public class AdminOrderHistoryServlet extends HttpServlet {

    private AdminOrderService adminOrderService;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.adminOrderService = (AdminOrderService) context.getAttribute("AdminOrderService");

        if (adminOrderService == null) {
            System.out.println("AdminOrderService not found in ServletContext!");
            throw new ServletException("AdminOrderService is not initialized.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<OrderDTO> orders = adminOrderService.getAllOrdersWithCustomerInfo();
            List<DeliveryPartnerDTO> deliveryPartners = adminOrderService.getAllDeliveryPartners();

            request.setAttribute(AttributeKeys.ORDERS, orders);
            request.setAttribute(AttributeKeys.DELIVERY_PARTNERS, deliveryPartners);

            request.getRequestDispatcher(PagePaths.ADMIN_ORDER_HISTORY).forward(request, response);

        } catch (Exception e) {
            System.out.println("Error retrieving admin order history: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.FAILED_TO_LOAD_ORDERS);
            request.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(request, response);
        }
    }
}
