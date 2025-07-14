package util;

import dto.ItemSearchCriteria;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class ItemSearchParser {

    public static ItemSearchCriteria parse(HttpServletRequest req) {
        ItemSearchCriteria.Builder builder = new ItemSearchCriteria.Builder();

        // Keyword
        String keyword = req.getParameter("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.keyword(keyword.trim());
        }

        // Category
        String categoryId = req.getParameter("category");
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            builder.categoryId(categoryId.trim());
        }

        // Min Price
        try {
            String minPriceStr = req.getParameter("minPrice");
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                builder.minPrice(new BigDecimal(minPriceStr.trim()));
            }
        } catch (NumberFormatException e) {
            // Ignore invalid minPrice param
        }

        // Max Price
        try {
            String maxPriceStr = req.getParameter("maxPrice");
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                builder.maxPrice(new BigDecimal(maxPriceStr.trim()));
            }
        } catch (NumberFormatException e) {
            // Ignore invalid maxPrice param
        }

        // Page
        try {
            String pageStr = req.getParameter("page");
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                int page = Integer.parseInt(pageStr.trim());
                builder.page(page);
            }
        } catch (NumberFormatException ignored) {
        }

        // Limit
        try {
            String limitStr = req.getParameter("limit");
            if (limitStr != null && !limitStr.trim().isEmpty()) {
                int limit = Integer.parseInt(limitStr.trim());
                builder.limit(limit);
            }
        } catch (NumberFormatException ignored) {
        }

        return builder.build();
    }
}
