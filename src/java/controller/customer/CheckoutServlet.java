package controller.customer;

import dto.UserSession;
import model.CartItem;
import dao.CartItemDAO;
import dao.CartItemDAOimpl;
import db.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class CheckoutServlet extends HttpServlet {

    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            cartItemDAO = new CartItemDAOimpl(conn);
        } catch (Exception e) {
            throw new ServletException("DB init failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("../login.jsp");
            return;
        }

        UserSession user = (UserSession) session.getAttribute("user");

        try {
            List<CartItem> cartItems = cartItemDAO.findByCustomerId(user.getId());

            BigDecimal total = BigDecimal.ZERO;

            for (CartItem item : cartItems) {
                BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setSubtotal(subtotal); // 🔧 Add this setter in CartItem.java
                total = total.add(subtotal);
            }

            session.setAttribute("cart", cartItems); // Optional if still used
            session.setAttribute("cartTotal", total);
            req.setAttribute("cartItems", cartItems); // ✅ For JSP
            req.setAttribute("cartTotal", total);

            req.getRequestDispatcher("/customer/checkout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Failed to load checkout", e);
        }
    }
}
