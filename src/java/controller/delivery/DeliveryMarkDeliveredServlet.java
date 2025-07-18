package controller.delivery;

import service.deliveryPartner.DeliveryOrderService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class DeliveryMarkDeliveredServlet extends HttpServlet {

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
            deliveryOrderService.markAsDelivered(orderId);
            response.sendRedirect("DeliveryPartnerOrderListServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update status or send email.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
