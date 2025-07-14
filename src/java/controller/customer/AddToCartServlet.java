package controller.customer;

import dto.UserSession;
import dto.ItemDTO;
import service.common.Validator;
import service.customer.CartService;
import service.customer.DiscountService;
import service.admin.ItemService;
import util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import dto.*;
import service.common.*;
import static util.contannts.SessionKeys.*;
import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.AttributeKeys.ERROR_MESSAGE;

public class AddToCartServlet extends BaseCustomerServlet {

    private DiscountService discountService;
    private ItemService itemService;
    private Validator validator;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.cartService = (CartService) getServletContext().getAttribute("CartService");
        this.itemService = (ItemService) getServletContext().getAttribute("ItemService");
        this.discountService = (DiscountService) getServletContext().getAttribute("DiscountService");
        this.validator = (Validator) getServletContext().getAttribute("AddToCartRequestValidator");

        if (cartService == null || itemService == null || discountService == null) {
            throw new ServletException("Failed to initialize AddToCartServlet due to missing dependencies");
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    UserSession user = getAuthenticatedUser(request, response);
    if (user == null) return;

    // Wrap request parameters in DTO
    AddToCartRequestDTO dto = new AddToCartRequestDTO();
    dto.setItemId(request.getParameter(ITEM_ID));
    dto.setQuantity(request.getParameter(QUANTITY));

    try {
        // Get validator from factory
        Validator<AddToCartRequestDTO> validator = ValidatorFactory.getValidator("addToCart");
        // Validate input
        validator.validate(dto);

        ItemDTO item = itemService.getItemById(dto.getItemId());
        if (item == null) {
            response.sendRedirect(BOOK_BROWSE_PAGE + "?error=" + ITEM_NOT_FOUND);
            return;
        }

        int quantity = Integer.parseInt(dto.getQuantity());

// No need to get effectivePrice here; service will handle it
cartService.addItem(user.getId(), item, quantity, request.getSession());

        request.getSession().setAttribute(SUCCESS_MESSAGE, "Item added to cart successfully.");
    } catch (ValidationException ve) {
        // Handle validation error gracefully
        request.setAttribute(ERROR_MESSAGE, ve.getMessage());
        request.getRequestDispatcher(BOOK_BROWSE_PAGE).forward(request, response);
        return;
    } catch (Exception e) {
        request.setAttribute(ERROR_MESSAGE, CART_LOAD_FAILED);
        e.printStackTrace();
    }

    response.sendRedirect(BOOK_BROWSE_SERVLET);
}

}
