package controller.customer;

import model.CartItem;
import dao.CartItemDAO;
import dao.CartItemDAOimpl;
import dto.UserSession;
import db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.customer.SessionCartManager;
import static util.contannts.ParameterKeys.*;
import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;
import service.customer.CartServiceImpl;
import service.customer.CartService;
public class UpdatecartServlet extends BaseCustomerServlet {

    private static final Logger logger = Logger.getLogger(UpdatecartServlet.class.getName());
    private CartItemDAO cartItemDAO;

  @Override
public void init() throws ServletException {
    this.cartService = (CartService) getServletContext().getAttribute("CartService");
    if (this.cartService == null) {
        throw new ServletException("CartService not found in ServletContext");
    }
}



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String itemId = req.getParameter(ITEM_ID);
        String quantityStr = req.getParameter(QUANTITY);

        if (itemId == null || quantityStr == null) {
            resp.sendRedirect(CART_PAGE + "?error=" + MISSING_PARAMS);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) quantity = 0;
        } catch (NumberFormatException e) {
            quantity = 0;
        }

        UserSession userSession = getAuthenticatedUser(req, resp);
        if (userSession == null) return;

        try {
           if (quantity <= 0) {
    cartService.removeCartItem(userSession.getId(), itemId); // Remove from DB
    cartService.removeCartItemFromSession(req.getSession(), itemId); // Remove from session
} else {
    cartService.updateCartItemQuantity(userSession.getId(), itemId, quantity); // Update DB
    cartService.updateCartItemQuantityInSession(req.getSession(), itemId, quantity); // Update session
}


            refreshCartInSession(req, userSession.getId());

            resp.sendRedirect(CART_PAGE + "?success=" + CART_UPDATED_SUCCESSFULLY);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "🛑 Failed to update cart", e);
            resp.sendRedirect(CART_PAGE + "?error=" + CART_UPDATE_FAILED);
        }
    }
}
