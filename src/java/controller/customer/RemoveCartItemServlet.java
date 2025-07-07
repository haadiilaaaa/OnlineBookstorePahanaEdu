package controller.customer;

import model.CartItem;
import dao.CartItemDAO;
import dao.CartItemDAOimpl;
import dto.UserSession;
import db.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

public class RemoveCartItemServlet extends HttpServlet {

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

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UserSession userSession = (UserSession) session.getAttribute("user");
        if (userSession == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");

        if (cart != null) {
            cart.remove(itemId);
            session.setAttribute("cart", cart);
            try {
                cartItemDAO.deleteByCustomerAndItem(userSession.getId(), itemId);
            } catch (Exception e) {
                throw new ServletException("Failed to remove cart item from DB", e);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/customer/cart.jsp");
    }
}
