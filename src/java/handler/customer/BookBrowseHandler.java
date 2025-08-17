package handler.customer;

import dto.CategoryDTO;
import dto.ItemDTO;
import dto.ItemSearchCriteria;
import dto.UserSession;
import model.CartItem;
import service.admin.ItemService;
import service.common.CategoryCache;
import service.customer.SessionCartService;
import util.ItemSearchParser;
import util.contannts.*;

import javax.servlet.http.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;  

public class BookBrowseHandler {

    private final ItemService itemService;
    private final CategoryCache categoryCache;
    private final SessionCartService sessionCartService;

    public BookBrowseHandler(ItemService itemService, CategoryCache categoryCache, SessionCartService sessionCartService) {
        this.itemService = itemService;
        this.categoryCache = categoryCache;
        this.sessionCartService = sessionCartService;
    }

    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        UserSession user = (UserSession) session.getAttribute(SessionKeys.USER);

        // Delegate cart loading to the SessionCartService
        sessionCartService.loadCartForUser(session, user.getId());

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