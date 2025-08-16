package util;

import dao.ItemDAO;
import model.CartItem;
import model.Item;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartUtil {

    /**
     * Calculates the total price of items in the cart.
     *
     * @param cart     Map of itemId to quantity.
     * @param itemDAO  DAO used to fetch item prices.
     * @return total cart price.
     */
    public static BigDecimal calculateCartTotal(Map<String, Integer> cart, ItemDAO itemDAO) {
        BigDecimal total = BigDecimal.ZERO;

        if (cart == null || itemDAO == null) {
            System.out.println("Cart or ItemDAO is null in calculateCartTotal()");
            return total;
        }

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();

            try {
                Item item = itemDAO.findById(itemId);
                if (item != null) {
                    total = total.add(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
                } else {
                    System.out.println("Item not found for ID: " + itemId);
                }
            } catch (Exception e) {
                System.out.println("Error fetching item for ID: " + itemId);
                e.printStackTrace();
            }
        }

        return total;
    }

    /**
     * Converts a List<CartItem> to a Map<itemId, CartItem>
     *
     * @param items List of CartItem
     * @return Map with itemId as key and CartItem as value
     */
    public static Map<String, CartItem> convertListToMap(List<CartItem> items) {
        Map<String, CartItem> map = new HashMap<>();
        if (items == null) return map;

        for (CartItem item : items) {
            if (item != null && item.getItemId() != null) {
                map.put(item.getItemId(), item);
            } else {
                System.out.println("Null item or itemId found in convertListToMap()");
            }
        }
        return map;
    }
}
