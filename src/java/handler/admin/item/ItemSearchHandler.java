package handler.admin.item;

import dto.ItemDTO;
import dto.ItemSearchCriteria;
import service.admin.ItemService;
import util.ItemSearchParser;
import util.contannts.AttributeKeys;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ItemSearchHandler {
    private final ItemService itemService;

    public ItemSearchHandler(ItemService itemService) {
        this.itemService = itemService;
    }

    public void handleSearch(HttpServletRequest req) throws Exception {
        ItemSearchCriteria criteria = ItemSearchParser.parse(req);
        List<ItemDTO> items = itemService.searchItems(
            criteria.getKeyword(),
            criteria.getCategoryId(),
            criteria.getMinPrice(),
            criteria.getMaxPrice()
        );
        req.setAttribute(AttributeKeys.ITEMS, items);
    }
}