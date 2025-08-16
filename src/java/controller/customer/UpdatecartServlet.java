package controller.customer;

import dto.UserSession;
import service.customer.CartFacade;
import util.contannts.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

import static util.contannts.ParameterKeys.ITEM_ID;
import static util.contannts.ParameterKeys.QUANTITY;
import static util.contannts.PagePaths.CART_PAGE;
import static util.contannts.ErrorMessages.*;

public class UpdatecartServlet extends BaseCustomerServlet {

    private CartFacade cartFacade;

    @Override
    public void init() throws ServletException {
        // The servlet now gets its facade from the ServletContext.
        this.cartFacade = (CartFacade) getServletContext().getAttribute(ContextKeys.CART_FACADE);
        
        if (this.cartFacade == null) {
            throw new ServletException("CartFacade not found in ServletContext.");
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
    // FIX: Redirect to the login page if the user is not authenticated.
    if (userSession == null) {
        resp.sendRedirect(PagePaths.LOGIN_PAGE); // Assumes LOGIN_PAGE is a constant
        return;
    }

    try {
        // All cart update logic is now handled by a single facade method.
        cartFacade.updateCartItem(userSession.getId(), itemId, quantity, req.getSession());
        
        resp.sendRedirect(CART_PAGE + "?success=" + CART_UPDATED_SUCCESSFULLY);
    } catch (Exception e) {
        System.out.println("🛑 Failed to update cart: " + e.getMessage());
        e.printStackTrace();
        resp.sendRedirect(CART_PAGE + "?error=" + CART_UPDATE_FAILED);
    }
}
}
