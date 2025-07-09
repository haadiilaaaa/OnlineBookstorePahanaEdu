package handler.customer;

import dto.CategoryDTO;
import dto.ItemDTO;
import dto.ItemSearchCriteria;
import dto.UserSession;
import model.CartItem;
import service.admin.ItemService;
import service.common.CategoryCache;
import service.customer.CartService;
import util.ItemSearchParser;
import util.contannts.*;

import javax.servlet.http.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BookBrowseHandler {

    private final ItemService itemService;
    private final CategoryCache categoryCache;
    private final CartService cartService;

    public BookBrowseHandler(ItemService itemService, CategoryCache categoryCache, CartService cartService) {
        this.itemService = itemService;
        this.categoryCache = categoryCache;
        this.cartService = cartService;
    }

    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(SessionKeys.USER) == null) {
            resp.sendRedirect(PagePaths.LOGIN_PAGE);
            return;
        }

        UserSession user = (UserSession) session.getAttribute(SessionKeys.USER);

        // Load or initialize cart map
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute(SessionKeys.CART);
        if (cart == null) {
            cart = cartService.getCartMapByCustomerId(user.getId());
            session.setAttribute(SessionKeys.CART, cart);
        }

        // Parse search/filter criteria from request
        ItemSearchCriteria criteria = ItemSearchParser.parse(req);

        // Fetch categories and items
        List<CategoryDTO> categories = categoryCache.getCategories();
        List<ItemDTO> items = itemService.searchItems(
                criteria.getKeyword(),
                criteria.getCategoryId(),
                criteria.getMinPrice(),
                criteria.getMaxPrice()
        );

        // Set request attributes for JSP
        req.setAttribute(AttributeKeys.CATEGORIES, categories);
        req.setAttribute(AttributeKeys.ITEMS, items);
        req.setAttribute(AttributeKeys.SELECTED_CATEGORY, criteria.getCategoryId());
        req.setAttribute(AttributeKeys.SEARCH_KEYWORD, criteria.getKeyword());
        req.setAttribute(AttributeKeys.MIN_PRICE, criteria.getMinPrice());
        req.setAttribute(AttributeKeys.MAX_PRICE, criteria.getMaxPrice());

        req.getRequestDispatcher(PagePaths.BOOK_BROWSE_PAGE).forward(req, resp);
    }
}
