package controller.delivery;

import dto.UserSession;
import service.deliveryPartner.DeliveryPartnerProfileService;
import service.deliveryPartner.DeliveryPartnerProfileServiceImpl;
import dao.DeliveryPartnerDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.*;   
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class DeliveryPartnerDashboardServlet extends HttpServlet {

    private DeliveryPartnerProfileService profileService;

    @Override
    public void init() throws ServletException {
        try {
            DeliveryPartnerDAO dpDAO = new dao.DeliveryPartnerDAOImpl(DBConnection.getInstance().getConnection());
            // Inject OrderItemDAO instance, required by OrderDAOImpl constructor
            OrderItemDAO orderItemDAO = new dao.OrderItemDAOImpl(DBConnection.getInstance().getConnection());
            OrderDAO orderDAO = new dao.OrderDAOImpl(DBConnection.getInstance().getConnection(), orderItemDAO);
            profileService = new DeliveryPartnerProfileServiceImpl(dpDAO, orderDAO);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize DAOs", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UserSession user = (UserSession) session.getAttribute("user");

        if (user == null || !"delivery".equals(user.getUserType())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        try {
            String partnerId = user.getId();

            BigDecimal earnings = profileService.getTotalEarnings(partnerId);
            int totalDeliveries = profileService.getTotalDeliveries(partnerId);
            int pendingDeliveries = profileService.getPendingDeliveries(partnerId);
            List<String> notifications = profileService.getNotifications(partnerId);

            req.setAttribute("earnings", earnings);
            req.setAttribute("totalDeliveries", totalDeliveries);
            req.setAttribute("pendingDeliveries", pendingDeliveries);
            req.setAttribute("notifications", notifications);

        } catch (Exception e) {
            req.setAttribute("error", "Failed to load dashboard data: " + e.getMessage());
        }

        req.getRequestDispatcher("/deliveryPartner/deliveryPartnerDashboard.jsp").forward(req, resp);
    }
}
