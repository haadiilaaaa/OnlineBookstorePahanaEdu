package controller.customer;

import model.CartItem;
import dao.CartItemDAO;
import dao.CartItemDAOimpl;
import dto.UserSession;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import util.*;

import db.DBConnection;

public class UpdatecartServlet extends HttpServlet {

    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            cartItemDAO = new CartItemDAOimpl(connection);
        } catch (Exception e) {
            throw new ServletException("DB connection error", e);
        }
    }

   @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String itemId = req.getParameter("itemId");
    String quantityStr = req.getParameter("quantity");

    if (itemId == null || quantityStr == null) {
        resp.sendRedirect("cart.jsp");
        return;
    }

    int quantity;
    try {
        quantity = Integer.parseInt(quantityStr);
    } catch (NumberFormatException e) {
        quantity = 1;
    }

    HttpSession session = req.getSession(false);
    if (session != null) {
        UserSession userSession = (UserSession) session.getAttribute("user");
        if (userSession == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            if (quantity <= 0) {
                cartItemDAO.deleteByCustomerAndItem(userSession.getId(), itemId);
            } else {
                cartItemDAO.updateQuantity(userSession.getId(), itemId, quantity);
            }

            // Reload the cart from DB after update
            var cartItems = cartItemDAO.findByCustomerId(userSession.getId());

            // Convert List<CartItem> to Map<String, CartItem>
            Map<String, CartItem> cart = CartUtil.convertListToMap(cartItems);

            // Update session cart attribute
            session.setAttribute("cart", cart);

        } catch (Exception e) {
            throw new ServletException("Failed to update cart in DB or reload cart", e);
        }
    }

    resp.sendRedirect("customer/cart.jsp");
}

}
