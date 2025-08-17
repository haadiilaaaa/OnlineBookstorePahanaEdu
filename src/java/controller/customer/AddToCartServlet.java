package controller.customer;

import dto.UserSession;
import dto.AddToCartRequestDTO;
import service.common.Validator;
import util.ValidationException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;  
import command.customer.cart.AddToCartCommand;
import static util.contannts.SessionKeys.*;
import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.AttributeKeys.ERROR_MESSAGE;
import service.customer.CartFacade; // Import the CartFacade

public class AddToCartServlet extends BaseCustomerServlet {

    private AddToCartCommand addToCartCommand;
    private Validator<AddToCartRequestDTO> validator;
    private CartFacade cartFacade; // New dependency

    @Override
    public void init() throws ServletException {
        this.addToCartCommand = (AddToCartCommand) getServletContext().getAttribute("AddToCartCommand");
        this.validator = (Validator<AddToCartRequestDTO>) getServletContext().getAttribute("AddToCartRequestValidator");
        this.cartFacade = (CartFacade) getServletContext().getAttribute("CartFacade"); // Retrieve the facade

        if (addToCartCommand == null || validator == null || cartFacade == null) {
            throw new ServletException("Failed to initialize AddToCartServlet due to missing dependencies");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserSession user = getAuthenticatedUser(request, response);
        if (user == null) {
            return;
        }

        AddToCartRequestDTO dto = new AddToCartRequestDTO();
        dto.setItemId(request.getParameter(ITEM_ID));
        dto.setQuantity(request.getParameter(QUANTITY));

        try {
            validator.validate(dto);

            // Step 1: Check if the item already exists in the cart for this user
            if (cartFacade.isItemInCart(user.getId(), dto.getItemId())) {
                request.getSession().setAttribute(ERROR_MESSAGE, "This item is already in your cart. You can update its quantity from the cart page.");
            } else {
                // Step 2: If the item is not in the cart, proceed with the command
                addToCartCommand.execute(dto, user, request.getSession());
                request.getSession().setAttribute(SUCCESS_MESSAGE, "Item added to cart successfully.");
            }

            response.sendRedirect(request.getContextPath() + BOOK_BROWSE_SERVLET);
        } catch (ValidationException ve) {
            request.getSession().setAttribute(ERROR_MESSAGE, ve.getMessage());
            response.sendRedirect(request.getContextPath() + BOOK_BROWSE_SERVLET);
        } catch (Exception e) {
            request.getSession().setAttribute(ERROR_MESSAGE, CART_LOAD_FAILED);
            response.sendRedirect(request.getContextPath() + BOOK_BROWSE_SERVLET);
            e.printStackTrace();
        }
    }
}