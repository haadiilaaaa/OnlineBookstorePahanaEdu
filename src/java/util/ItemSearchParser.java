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

        // Category ✅ FIXED: treat empty string as null
        String categoryId = req.getParameter("category");
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            criteria.setCategoryId(categoryId.trim());
        } else {
            criteria.setCategoryId(null); // <-- this is what was missing
        }

        // Min Price
        try {
            String minPriceStr = req.getParameter("minPrice");
            if (minPriceStr != null && !minPriceStr.isEmpty()) {
                criteria.setMinPrice(new BigDecimal(minPriceStr));
            }
        } catch (NumberFormatException e) {
            criteria.setMinPrice(null); // or log
        }

        // Max Price
        try {
            String maxPriceStr = req.getParameter("maxPrice");
            if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
                criteria.setMaxPrice(new BigDecimal(maxPriceStr));
            }
        } catch (NumberFormatException e) {
            criteria.setMaxPrice(null); // or log
        }

        return criteria;
    }
}
