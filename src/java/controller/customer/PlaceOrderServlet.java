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
    if (user == null) {
        System.out.println("DEBUG: User session is null, redirecting to login.");
        return;
    }
        

        HttpSession session = req.getSession();
      
     
    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute(CART);

    if (cart == null || cart.isEmpty()) {
        System.out.println("DEBUG: Cart is empty, redirecting to customer dashboard.");
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
            
             System.out.println("DEBUG: Processing order for user " + user.getId());
        System.out.println("DEBUG: Shipping Address: " + shippingAddress);
        System.out.println("DEBUG: Payment Method: " + paymentMethod);

            OrderDTO order = orderFacadeService.processOrder(req, user, cart, shippingAddress, paymentMethod);
            System.out.println("DEBUG: Order successfully processed with ID: " + order.getOrderId());

            // ✅ Clear cart
            session.removeAttribute(CART);

            // ✅ Prepare invoice and attach attributes
            OrderInvoiceHelper.prepareInvoiceData(req, order, user, order.getItems(), invoiceService, itemDAO, order.getDeliveryFare());


            session.setAttribute(ORDER_ID, order.getOrderId());
            System.out.println("DEBUG: Cart removed from session. Order ID set in session: " + session.getAttribute(ORDER_ID));
              System.out.println("DEBUG: Redirecting to OrderConfirmationServlet at path: " + req.getContextPath() + ORDER_CONFIRMATION);

            resp.sendRedirect(req.getContextPath() + ORDER_CONFIRMATION);

        } catch (Exception e) {
            System.err.println("DEBUG: An error occurred during order placement.");
        e.printStackTrace();
        req.setAttribute("error", "⚠️ Failed to place order. Please try again.");
        req.getRequestDispatcher(CHECKOUT_PAGE).forward(req, resp);
        }
    }
}