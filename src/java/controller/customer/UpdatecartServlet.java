package controller.customer;

import model.CartItem;
import dao.CartItemDAO;
import dao.CartItemDAOimpl;
import dto.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import db.DBConnection;
import util.CartUtil;

import static util.contannts.SessionKeys.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;

public class UpdatecartServlet extends BaseCustomerServlet {

    private static final Logger logger = Logger.getLogger(UpdatecartServlet.class.getName());
    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            cartItemDAO = new CartItemDAOimpl(connection);
            this.cartService = new service.customer.CartServiceImpl(cartItemDAO);
            logger.info("✅ UpdateCartServlet initialized successfully.");
        } catch (Exception e) {
            throw new ServletException("❌ DB connection error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String itemId = req.getParameter(ITEM_ID);
        String quantityStr = req.getParameter(QUANTITY);

        if (itemId == null || quantityStr == null) {
            redirectWithMessage(resp, "error", MISSING_PARAMS);
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

        logger.info("🔁 User " + userSession.getId() + " updating item " + itemId + " to qty " + quantity);

        try {
            if (quantity <= 0) {
                cartItemDAO.deleteByCustomerAndItem(userSession.getId(), itemId);
                logger.info("🗑️ Removed item " + itemId + " from cart");
            } else {
                cartItemDAO.updateQuantity(userSession.getId(), itemId, quantity);
                logger.info("✅ Updated item " + itemId + " quantity to " + quantity);
            }

            // Refresh cart in session
            refreshCartInSession(req, userSession.getId());

            redirectWithMessage(resp, "success", CART_UPDATED_SUCCESSFULLY);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "🛑 Failed to update cart for user " + userSession.getId(), e);
            redirectWithMessage(resp, "error", CART_UPDATE_FAILED);
        }
    }

    // Helper method to redirect with a message parameter
    private void redirectWithMessage(HttpServletResponse resp, String type, String message) throws IOException {
        resp.sendRedirect(CART_PAGE + "?" + type + "=" + message);
    }
}
