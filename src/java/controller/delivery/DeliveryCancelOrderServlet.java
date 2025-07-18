package controller.delivery;

import service.deliveryPartner.DeliveryOrderService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class DeliveryCancelOrderServlet extends HttpServlet {

    private DeliveryOrderService deliveryOrderService;

    @Override
    public void init() throws ServletException {
        deliveryOrderService = (DeliveryOrderService) getServletContext().getAttribute("DeliveryOrderService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderId = request.getParameter("orderId");

        try {
            deliveryOrderService.cancelOrder(orderId);
            // Optional: send email to customer about cancellation here
            response.sendRedirect("DeliveryPartnerOrderListServlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to cancel order.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
