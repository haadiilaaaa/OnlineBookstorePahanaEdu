package service.customer;

import dao.CartItemDAO;
import model.CartItem;
import util.IDGenerator;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static util.contannts.SessionKeys.CART;

public class CartServiceImpl implements PersistentCartService, SessionCartService {

    private final CartItemDAO cartItemDAO;
    private final SessionCartManager sessionCartManager;
    private final IDGenerator<String> cartIdGenerator;

    public CartServiceImpl(CartItemDAO cartItemDAO, SessionCartManager sessionCartManager, IDGenerator<String> cartIdGenerator) {
        this.cartItemDAO = cartItemDAO;
        this.sessionCartManager = sessionCartManager;
        this.cartIdGenerator = cartIdGenerator;
    }

    // --- PersistentCartService implementations ---
    @Override
    public Map<String, CartItem> getCartMapByCustomerId(String customerId) throws Exception {
        List<CartItem> cartItems = cartItemDAO.findByCustomerId(customerId);
        return cartItems.stream()
                .collect(Collectors.toMap(
                        CartItem::getItemId,
                        item -> item,
                        (existing, duplicate) -> {
                            existing.setQuantity(existing.getQuantity() + duplicate.getQuantity());
                            return existing;
                        }
                ));
    }

    // Inside CartServiceImpl
   // Inside service.customer.CartServiceImpl
       // In CartServiceImpl.java
    // In CartServiceImpl.java
       @Override
public void addCartItem(String customerId, String itemId, int quantity, BigDecimal price) throws Exception {
    System.out.println("DEBUG: Starting addCartItem for customer " + customerId + " and item " + itemId);
    CartItem existingItem = cartItemDAO.findByCustomerAndItem(customerId, itemId);

    if (existingItem != null) {
        System.out.println("DEBUG: Item " + itemId + " found in cart. Updating quantity.");
        int newQuantity = existingItem.getQuantity() + quantity;
        cartItemDAO.updateQuantity(customerId, itemId, newQuantity);
        System.out.println("DEBUG: Updated quantity for item " + itemId + " to " + newQuantity);
    } else {
        System.out.println("DEBUG: Item " + itemId + " not found in cart. Generating new ID and saving.");
        String cartItemId = cartIdGenerator.generate(); 
        System.out.println("DEBUG: Generated new cart ID: " + cartItemId);
        CartItem dbItem = new CartItem(cartItemId, customerId, itemId, quantity);
        dbItem.setPrice(price);
        cartItemDAO.save(dbItem);
        System.out.println("DEBUG: Successfully saved new cart item with ID: " + cartItemId);
    }
    System.out.println("DEBUG: Finished addCartItem for customer " + customerId + " and item " + itemId);
}

    @Override
    public void updateCartItemQuantity(String customerId, String itemId, int quantity) throws Exception {
        cartItemDAO.updateQuantity(customerId, itemId, quantity);
    }

    @Override
    public void removeCartItem(String customerId, String itemId) throws Exception {
        cartItemDAO.deleteByCustomerAndItem(customerId, itemId);
    }

    @Override
    public void clearCart(String customerId) throws Exception {
        cartItemDAO.deleteCartItemsByUserId(customerId);
    }
    
    // --- SessionCartService implementations ---
    @Override
    public void loadCartForUser(HttpSession session, String customerId) throws Exception {
        if (session.getAttribute(CART) == null) {
            Map<String, CartItem> cartMap = getCartMapByCustomerId(customerId);
            session.setAttribute(CART, cartMap);
        }
    }

    @Override
    public void addToCartInSession(HttpSession session, String itemId, String title, BigDecimal price, int quantity, String imageUrl, BigDecimal originalPrice) {
        sessionCartManager.addItemToCart(session, itemId, title, price, quantity, imageUrl, originalPrice);
    }

    @Override
    public void updateCartItemQuantityInSession(HttpSession session, String itemId, int quantity) {
        sessionCartManager.updateItemQuantity(session, itemId, quantity);
    }

    @Override
    public void removeCartItemFromSession(HttpSession session, String itemId) {
        sessionCartManager.removeItemFromCart(session, itemId);
    }

    @Override
    public void clearCartInSession(HttpSession session) {
        sessionCartManager.clearCart(session);
    }
    
    @Override
public CartItem findByCustomerAndItem(String customerId, String itemId) throws Exception {
    return cartItemDAO.findByCustomerAndItem(customerId, itemId);
}
}