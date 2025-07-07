package util;

import dao.ItemDAO;
import model.Item;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;
import model.*;
import java.util.HashMap;
import java.util.List;
public class CartUtil {

    public static BigDecimal calculateCartTotal(Map<String, Integer> cart, ItemDAO itemDAO) throws Exception {
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();

            Item item = itemDAO.findById(itemId);
            if (item != null) {
                total = total.add(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
        }

        return total;
    }
    
    public static Map<String, CartItem> convertListToMap(List<CartItem> items) {
        Map<String, CartItem> map = new HashMap<>();
        for (CartItem item : items) {
            map.put(item.getItemId(), item);
        }
        return map;
    }

}
