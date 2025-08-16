package strategy.admin.item;

import service.admin.CategoryService;
import service.admin.ItemService;
import util.IDGenerator;
import util.NextSequentialIDGenerator;
import dto.ItemDTO; // Import the DTO to access the ID

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemStrategyRegistrar {

    public static Map<String, ItemActionStrategy> registerAll(ItemService itemService, CategoryService categoryService) throws Exception {
        Map<String, ItemActionStrategy> map = new HashMap<>();

        // Fetch existing item IDs to create the IDGenerator
        List<String> existingItemIds = itemService.getAllItems().stream()
            .map(ItemDTO::getId)
            .collect(Collectors.toList());
        IDGenerator<String> itemIdGenerator = new NextSequentialIDGenerator("item", existingItemIds);
        
        // Correctly instantiate AddItemStrategy with both dependencies
        map.put("add", new AddItemStrategy(itemService, itemIdGenerator));
        
        map.put("edit", new LoadEditItemStrategy(itemService, categoryService));
        map.put("update", new EditItemStrategy(itemService));
        map.put("delete", new DeleteItemStrategy(itemService));
        map.put("default", new DefaultItemStrategy());

        return map;
    }
}