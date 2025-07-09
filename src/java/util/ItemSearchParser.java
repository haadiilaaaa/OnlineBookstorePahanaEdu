package util;

import dto.ItemSearchCriteria;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class ItemSearchParser {

    public static ItemSearchCriteria parse(HttpServletRequest req) {
        ItemSearchCriteria criteria = new ItemSearchCriteria();

        // Keyword
        String keyword = req.getParameter("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            criteria.setKeyword(keyword.trim());
        }

        // Category
        String categoryId = req.getParameter("category");
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            criteria.setCategoryId(categoryId.trim());
        } else {
            criteria.setCategoryId(null);
        }

        // Min Price
        try {
            String minPriceStr = req.getParameter("minPrice");
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                criteria.setMinPrice(new BigDecimal(minPriceStr.trim()));
            }
        } catch (NumberFormatException e) {
            criteria.setMinPrice(null);
        }

        // Max Price
        try {
            String maxPriceStr = req.getParameter("maxPrice");
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                criteria.setMaxPrice(new BigDecimal(maxPriceStr.trim()));
            }
        } catch (NumberFormatException e) {
            criteria.setMaxPrice(null);
        }

        // ✅ Page
        try {
            String pageStr = req.getParameter("page");
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                int page = Integer.parseInt(pageStr.trim());
                if (page > 0) {
                    criteria.setPage(page);
                }
            }
        } catch (NumberFormatException ignored) {
        }

        // ✅ Limit
        try {
            String limitStr = req.getParameter("limit");
            if (limitStr != null && !limitStr.trim().isEmpty()) {
                int limit = Integer.parseInt(limitStr.trim());
                if (limit > 0 && limit <= 100) { // optional cap
                    criteria.setLimit(limit);
                }
            }
        } catch (NumberFormatException ignored) {
        }

        return criteria;
    }
}
