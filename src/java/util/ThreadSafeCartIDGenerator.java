package util;

import dao.CartItemDAO;
import model.CartItem; // Assuming your model is in the 'model' package
import java.util.List;
import java.util.stream.Collectors;

public class ThreadSafeCartIDGenerator implements IDGenerator<String> {

    private final String prefix;
    private final CartItemDAO cartItemDAO;

    public ThreadSafeCartIDGenerator(String prefix, CartItemDAO cartItemDAO) {
        this.prefix = prefix;
        this.cartItemDAO = cartItemDAO;
    }

    @Override
    public synchronized String generate() {
        try {
            List<CartItem> allItems = cartItemDAO.findAll();
            
            int maxNumber = allItems.stream()
                .map(CartItem::getId)
                .filter(id -> id.startsWith(prefix))
                .map(id -> id.substring(prefix.length()))
                .mapToInt(id -> {
                    try {
                        return Integer.parseInt(id);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);

            return String.format("%s%03d", prefix, maxNumber + 1);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate unique cart ID", e);
        }
    }
}