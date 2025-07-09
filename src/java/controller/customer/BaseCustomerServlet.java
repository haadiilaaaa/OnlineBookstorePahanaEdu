package controller.customer;

import dto.UserSession;
import model.CartItem;
import service.customer.CartService;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import static util.contannts.PagePaths.LOGIN_PAGE;
import static util.contannts.SessionKeys.CART;
import static util.contannts.SessionKeys.USER;

public abstract class BaseCustomerServlet extends HttpServlet {

    protected CartService cartService;

    protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(USER) == null) {
            resp.sendRedirect(LOGIN_PAGE);
            return null;
        }
        return (UserSession) session.getAttribute(USER);
    }

    protected void ensureCartLoaded(HttpServletRequest req, String customerId) throws Exception {
        HttpSession session = req.getSession();
        if (session.getAttribute(CART) == null) {
            Map<String, CartItem> cart = cartService.getCartMapByCustomerId(customerId);
            session.setAttribute(CART, cart);
        }
    }

    // ✅ NEW: Utility method to refresh cart in session
    protected void refreshCartInSession(HttpServletRequest req, String customerId) throws Exception {
        Map<String, CartItem> updatedCart = cartService.getCartMapByCustomerId(customerId);
        req.getSession().setAttribute(CART, updatedCart);
    }
}
