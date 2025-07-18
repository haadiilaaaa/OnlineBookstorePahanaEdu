package controller.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;
import dto.UserSession;
import service.customer.InvoiceService;
import dao.ItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import service.customer.*;

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
        if (user == null) return;

        HttpSession session = req.getSession(false);
        String orderId = (session != null) ? (String) session.getAttribute(ORDER_ID) : null;

        if (orderId == null) {
            resp.sendRedirect(req.getContextPath() + "/Customer_DashboardServlet");
            return;
        }

        try {
            Optional<OrderDTO> orderOpt = orderDAO.findOrderById(orderId);
            if (orderOpt.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/Customer_DashboardServlet");
                return;
            }

            OrderDTO order = orderOpt.get();

            if (!order.getUserId().equals(user.getId())) {
                resp.sendRedirect(req.getContextPath() + "/Customer_DashboardServlet");
                return;
            }

         List<OrderItemDTO> items = orderItemDAO.findItemsByOrderId(orderId);

BigDecimal deliveryFare = order.getDeliveryFare(); // NEW LINE

OrderInvoiceHelper.prepareInvoiceData(req, order, user, items, invoiceService, itemDAO, deliveryFare);

            req.setAttribute("order", order); 


            session.removeAttribute(ORDER_ID);
            req.getRequestDispatcher(THANK_YOU_PAGE).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/Customer_DashboardServlet");
        }
    }
}