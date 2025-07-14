package controller.customer;

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
import service.customer.DiscountService;
import mapper.ItemMapper;
import service.customer.CartServiceImpl;

public class RemoveCartItemServlet extends BaseCustomerServlet {

    private static final Logger logger = Logger.getLogger(RemoveCartItemServlet.class.getName());
    private CartItemDAO cartItemDAO;

   @Override
public void init() throws ServletException {
    try {
        Connection connection = DBConnection.getInstance().getConnection();
        cartItemDAO = new CartItemDAOimpl(connection);

        SessionCartManager sessionCartManager = new SessionCartManager();

        DiscountService discountService = (DiscountService) getServletContext().getAttribute("DiscountService");
        ItemMapper itemMapper = new ItemMapper(); // or fetch from context if you registered it

        this.cartService = new CartServiceImpl(cartItemDAO, sessionCartManager, discountService, itemMapper);
    } catch (Exception e) {
        logger.log(Level.SEVERE, "❌ Init failed", e);
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
            refreshCartInSession(req, userSession.getId());
            resp.sendRedirect(CART_PAGE + "?success=" + ITEM_REMOVED_SUCCESSFULLY);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "🛑 Remove failed", e);
            resp.sendRedirect(CART_PAGE + "?error=" + ITEM_REMOVE_FAILED);
        }
    }
}
