package strategy.admin.item;

import service.admin.CategoryService;
import service.admin.ItemService;

import java.util.HashMap;
import java.util.Map;

public class ItemStrategyRegistrar {

    public static Map<String, ItemActionStrategy> registerAll(ItemService itemService, CategoryService categoryService) {
        Map<String, ItemActionStrategy> map = new HashMap<>();

        map.put("add", new AddItemStrategy(itemService));
        map.put("edit", new LoadEditItemStrategy(itemService, categoryService));
        map.put("update", new EditItemStrategy(itemService)); // change this
        map.put("delete", new DeleteItemStrategy(itemService));
        map.put("default", new DefaultItemStrategy());

        return map;
    }
}
