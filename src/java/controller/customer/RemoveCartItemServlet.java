package controller.customer;

import model.CartItem;
import dao.CartItemDAO;
import dao.CartItemDAOimpl;
import dto.UserSession;
import db.DBConnection;
import util.CartUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.contannts.SessionKeys.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;

public class RemoveCartItemServlet extends BaseCustomerServlet {

    private static final Logger logger = Logger.getLogger(RemoveCartItemServlet.class.getName());
    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            cartItemDAO = new CartItemDAOimpl(connection);
            this.cartService = new service.customer.CartServiceImpl(cartItemDAO); // From base class
            logger.info("✅ RemoveCartItemServlet initialized successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Failed to initialize RemoveCartItemServlet", e);
            throw new ServletException("DB connection error", e);
        }
    }

    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String itemId = req.getParameter(ITEM_ID);

    UserSession userSession = getAuthenticatedUser(req, resp);
    if (userSession == null) return;

    try {
        cartItemDAO.deleteByCustomerAndItem(userSession.getId(), itemId);
        logger.info("🗑️ Removed item " + itemId + " from DB cart for user " + userSession.getId());

        // Use refactored method
        refreshCartInSession(req, userSession.getId());

        resp.sendRedirect(CART_PAGE + "?success=" + ITEM_REMOVED_SUCCESSFULLY);

    } catch (Exception e) {
        logger.log(Level.SEVERE, "🛑 Failed to remove cart item", e);
        resp.sendRedirect(CART_PAGE + "?error=" + ITEM_REMOVE_FAILED);
    }
}

}
