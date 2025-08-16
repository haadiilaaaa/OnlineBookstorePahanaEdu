package controller.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;
import dto.UserSession;
import service.customer.InvoiceService;
import dao.ItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import model.Item;
import util.contannts.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import service.customer.*;
import static util.contannts.PagePaths.CUSTOMER_DASHBOARD;
import static util.contannts.SessionKeys.ORDER_ID;
import static util.contannts.PagePaths.THANK_YOU_PAGE;

public class OrderConfirmationServlet extends BaseCustomerServlet {

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private InvoiceService invoiceService;
    private ItemDAO itemDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = (OrderDAO) getServletContext().getAttribute("OrderDAO");
        orderItemDAO = (OrderItemDAO) getServletContext().getAttribute("OrderItemDAO");
        invoiceService = (InvoiceService) getServletContext().getAttribute("InvoiceService");
        itemDAO = (ItemDAO) getServletContext().getAttribute("ItemDAO");

        if (orderDAO == null || orderItemDAO == null || invoiceService == null || itemDAO == null) {
            throw new ServletException("❌ Required services not found in servlet context.");
        }
    }

   

    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    UserSession user = getAuthenticatedUser(req, resp);
    if (user == null) {
        System.out.println("DEBUG: OrderConfirmationServlet: User session is null, redirecting.");
        return;
    }

    HttpSession session = req.getSession(false);
    String orderId = (session != null) ? (String) session.getAttribute(ORDER_ID) : null;

    System.out.println("DEBUG: OrderConfirmationServlet: Retrieved ORDER_ID from session: " + orderId);

    if (orderId == null) {
        System.out.println("DEBUG: OrderConfirmationServlet: Order ID is null, redirecting to dashboard.");
        resp.sendRedirect(req.getContextPath() + CUSTOMER_DASHBOARD);
        return;
    }

    try {
        Optional<OrderDTO> orderOpt = orderDAO.findOrderById(orderId);
        if (orderOpt.isEmpty()) {
            System.out.println("DEBUG: OrderConfirmationServlet: Order with ID " + orderId + " not found in DB.");
            resp.sendRedirect(req.getContextPath() + CUSTOMER_DASHBOARD);
            return;
        }

        OrderDTO order = orderOpt.get();
        System.out.println("DEBUG: OrderConfirmationServlet: Found order for ID " + orderId);

        if (!order.getUserId().equals(user.getId())) {
            System.out.println("DEBUG: OrderConfirmationServlet: User ID mismatch. Redirecting.");
            resp.sendRedirect(req.getContextPath() + CUSTOMER_DASHBOARD);
            return;
        }

        List<OrderItemDTO> items = orderItemDAO.findItemsByOrderId(orderId);
        System.out.println("DEBUG: OrderConfirmationServlet: Found " + items.size() + " items for order.");

        BigDecimal deliveryFare = order.getDeliveryFare();
        OrderInvoiceHelper.prepareInvoiceData(req, order, user, items, invoiceService, itemDAO, deliveryFare);

        // This is the correct, single forward.
        session.removeAttribute(ORDER_ID);
        req.getRequestDispatcher(THANK_YOU_PAGE).forward(req, resp);
        System.out.println("DEBUG: OrderConfirmationServlet: Forwarding to thank you page.");

    } catch (Exception e) {
        System.err.println("DEBUG: OrderConfirmationServlet: An error occurred.");
        e.printStackTrace();
        resp.sendRedirect(req.getContextPath() + CUSTOMER_DASHBOARD);
    }
}
    }

    
