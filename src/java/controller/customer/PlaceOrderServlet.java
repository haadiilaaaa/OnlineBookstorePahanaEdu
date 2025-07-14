package controller.customer;

import dto.OrderDTO;
import dto.UserSession;
import model.CartItem;
import service.customer.OrderFacadeService;
import static util.contannts.SessionKeys.*;
import static util.contannts.PagePaths.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import dao.*;
import service.customer.*;


public class PlaceOrderServlet extends BaseCustomerServlet {

    private OrderFacadeService orderFacadeService;
    private InvoiceService invoiceService;
    private ItemDAO itemDAO;

    @Override
    public void init() throws ServletException {
        orderFacadeService = (OrderFacadeService) getServletContext().getAttribute("OrderFacadeService");
        invoiceService = (InvoiceService) getServletContext().getAttribute("InvoiceService");
        itemDAO = (ItemDAO) getServletContext().getAttribute("ItemDAO");

        if (orderFacadeService == null || invoiceService == null || itemDAO == null) {
            throw new ServletException("❌ Required services not found in servlet context.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession user = getAuthenticatedUser(req, resp);
        if (user == null) return;

        HttpSession session = req.getSession();
      
     
    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute(CART);

if (cart == null || cart.isEmpty()) {
    resp.sendRedirect(CUSTOMER_DASHBOARD_PAGE);
    return;
}



        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(CUSTOMER_DASHBOARD_PAGE);
            return;
        }

        try {
            String shippingAddress = req.getParameter("shippingAddress");
            String paymentMethod = req.getParameter("paymentMethod");

            OrderDTO order = orderFacadeService.processOrder(req, user, cart, shippingAddress, paymentMethod);

            // ✅ Clear cart
            session.removeAttribute(CART);

            // ✅ Prepare invoice and attach attributes
            OrderInvoiceHelper.prepareInvoiceData(req, order, user, order.getItems(), invoiceService, itemDAO);

            session.setAttribute(ORDER_ID, order.getOrderId());

            resp.sendRedirect(req.getContextPath() + ORDER_CONFIRMATION);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "⚠️ Failed to place order. Please try again.");
            req.getRequestDispatcher(CHECKOUT_PAGE).forward(req, resp);
        }
    }
}