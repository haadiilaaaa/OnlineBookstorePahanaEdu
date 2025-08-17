package controller.customer;

import dao.CartItemDAO;
import dao.CartItemDAOimpl;
import dto.UserSession;
import db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

import service.customer.SessionCartManager;
import static util.contannts.ParameterKeys.*;
import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;
import service.customer.DiscountService;
import mapper.ItemMapper;  
import service.customer.CartFacade;
import service.customer.CartServiceImpl;
import service.customer.PersistentCartService;
import service.customer.SessionCartService;
import service.customer.ItemServiceFactory;
import service.admin.DiscountManagementService;

public class RemoveCartItemServlet extends BaseCustomerServlet {

    protected CartFacade cartFacade;

    @Override
    public void init() throws ServletException {
        // We'll rely on the ItemServiceFactory to create our CartFacade,
        // which encapsulates all the cart logic.
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            this.cartFacade = ItemServiceFactory.createCartFacade(connection);
        } catch (Exception e) {
            System.out.println("❌ Init failed: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("DB connection error or CartFacade creation failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String itemId = req.getParameter(ITEM_ID);
        UserSession userSession = getAuthenticatedUser(req, resp);

        // Make sure a user is authenticated and the item ID is not null
        if (userSession == null || itemId == null || itemId.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?error=invalidRequest");
            return;
        }

        // Get a new database connection for this request
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            // Create a new CartFacade for this request using the fresh connection
            CartFacade cartFacade = ItemServiceFactory.createCartFacade(connection);

            // Use the facade to remove the item and refresh the session
            cartFacade.removeCartItemAndRefreshSession(userSession.getId(), itemId, req.getSession());

            // Redirect with a success message
            resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?success=itemRemoved");
        } catch (Exception e) {
            System.out.println("🛑 Failed to remove item from cart: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?error=cartUpdateFailed");
        }
    }
}
