package controller.delivery;

import dto.OrderDTO;
import service.deliveryPartner.DeliveryOrderService;
import util.contannts.PagePaths;
import util.contannts.AttributeKeys;
import dto.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class DeliveryPartnerOrderListServlet extends HttpServlet {

    private DeliveryOrderService deliveryOrderService;

    @Override
    public void init() throws ServletException {
        deliveryOrderService = (DeliveryOrderService) getServletContext().getAttribute("DeliveryOrderService");
        if (deliveryOrderService == null) {
            throw new ServletException("DeliveryOrderService not initialized");
        }
    }
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    UserSession userSession = (UserSession) session.getAttribute("user");
    String partnerId = userSession.getId(); // ✅ FIXED

    try {
        List<OrderDTO> orders = deliveryOrderService.getOrdersAssignedToPartner(partnerId);
        request.setAttribute(AttributeKeys.ORDERS, orders);
        request.getRequestDispatcher(PagePaths.DELIVERY_ASSIGNED_ORDERS).forward(request, response);
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("error", "Failed to load orders.");
        request.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(request, response);
    }
}


}
