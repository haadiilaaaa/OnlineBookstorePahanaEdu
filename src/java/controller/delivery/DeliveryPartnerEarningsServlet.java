package controller.delivery;

import service.deliveryPartner.DeliveryPartnerProfileService;
import service.deliveryPartner.DeliveryPartnerProfileServiceImpl;
import dao.DeliveryPartnerDAO;
import dao.OrderDAO;
import db.DBConnection;
import java.sql.Connection;
import dao.OrderItemDAO;
import javax.servlet.ServletException;
import javax.servlet.http.*;  
import java.io.IOException;
import java.math.BigDecimal;

public class DeliveryPartnerEarningsServlet extends HttpServlet {

    private DeliveryPartnerProfileService profileService;

    @Override
public void init() throws ServletException {
    try {
        Connection conn = DBConnection.getInstance().getConnection();
        DeliveryPartnerDAO dpDAO = new dao.DeliveryPartnerDAOImpl(conn);
        OrderItemDAO orderItemDAO = new dao.OrderItemDAOImpl(conn);  // create OrderItemDAO impl instance
        OrderDAO orderDAO = new dao.OrderDAOImpl(conn, orderItemDAO); // pass both
        profileService = new DeliveryPartnerProfileServiceImpl(dpDAO, orderDAO);
    } catch (Exception e) {
        throw new ServletException(e);
    }
}


    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String partnerId = ((dto.UserSession) session.getAttribute("user")).getId();

    try {
        BigDecimal earnings = profileService.getTotalEarnings(partnerId);
        int totalDeliveries = profileService.getTotalDeliveries(partnerId);

        request.setAttribute("earnings", earnings);
        request.setAttribute("totalDeliveries", totalDeliveries);

        request.getRequestDispatcher("deliveryPartner/deliveryPartnerEarnings.jsp").forward(request, response);
    } catch (Exception e) {
        request.setAttribute("error", "Could not fetch earnings and deliveries: " + e.getMessage());
        request.getRequestDispatcher("/deliveryPartnerEarnings.jsp").forward(request, response);
    }
}

}
