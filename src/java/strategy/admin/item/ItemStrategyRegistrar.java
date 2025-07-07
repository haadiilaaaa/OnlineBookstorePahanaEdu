package strategy.admin.item;

import service.admin.CategoryService;
import service.admin.ItemService;


import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import db.DBConnection;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ItemStrategyRegistrar {

   public static Map<String, ItemActionStrategy> registerAll(ItemService itemService, CategoryService categoryService) {
    Map<String, ItemActionStrategy> map = new HashMap<>();
    try {
        Connection conn = DBConnection.getInstance().getConnection();
        CategoryDAO categoryDAO = new CategoryDAOImpl(conn);

        map.put("add", new AddItemStrategy(itemService));
        map.put("edit", new LoadEditItemStrategy(itemService, categoryService));
        map.put("update", new EditItemStrategy(itemService, categoryDAO));
        map.put("delete", new DeleteItemStrategy(itemService));
        map.put("default", new DefaultItemStrategy());

    } catch (Exception e) {
        e.printStackTrace();
        // Optionally: throw RuntimeException to propagate or return empty map
        throw new RuntimeException("Failed to register strategies", e);
    }
    return map;
}

}
