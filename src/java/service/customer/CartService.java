package service.customer;

import model.CartItem;
import dto.*;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

public interface CartService {

    // Get the customer's cart items from the database as a map (itemId -> CartItem)
    Map<String, CartItem> getCartMapByCustomerId(String customerId) throws Exception;

    // Load the cart for the user into the HttpSession if not already loaded
    void loadCartForUser(HttpSession session, String customerId) throws Exception;

    // Add an item to the persistent cart (database)
    void addCartItem(String customerId, String itemId, int quantity, BigDecimal price) throws Exception;

    // Update quantity of an item in persistent cart
    void updateCartItemQuantity(String customerId, String itemId, int quantity) throws Exception;

    // Remove an item from persistent cart
    void removeCartItem(String customerId, String itemId) throws Exception;

    // Clear the entire persistent cart for a customer
    void clearCart(String customerId) throws Exception;

    // Add an item to the session cart
    void addToCartInSession(HttpSession session, String itemId, String title, BigDecimal price, int quantity, String imageUrl, BigDecimal originalPrice);

    // Update quantity of an item in the session cart
    void updateCartItemQuantityInSession(HttpSession session, String itemId, int quantity);

    // Remove an item from the session cart
    void removeCartItemFromSession(HttpSession session, String itemId);

    // Clear the session cart
    void clearCartInSession(HttpSession session);
    // In CartService.java
// CartService.java
void addItem(String userId, ItemDTO item, int quantity, HttpSession session) throws Exception;



}

