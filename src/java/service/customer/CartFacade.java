package service.customer;

import dto.ItemDTO;
import mapper.ItemMapper;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import model.*;

public class CartFacade {  
    private final PersistentCartService persistentCartService;
    private final SessionCartService sessionCartService;
    private final DiscountService discountService;
    private final ItemMapper itemMapper;

    public CartFacade(PersistentCartService persistentCartService, SessionCartService sessionCartService, DiscountService discountService, ItemMapper itemMapper) {
        this.persistentCartService = persistentCartService;
        this.sessionCartService = sessionCartService;
        this.discountService = discountService;
        this.itemMapper = itemMapper;
    }

    public void addItemToCart(String userId, ItemDTO itemDto, int quantity, HttpSession session) throws Exception {
        BigDecimal effectivePrice = discountService.applyBestDiscount(itemMapper.toItem(itemDto));

        // Attempt to update the persistent cart first.
        // If this fails, we don't touch the session cart.
        try {
            persistentCartService.addCartItem(userId, itemDto.getId(), quantity, effectivePrice);
            
            // If the persistent cart update succeeds, then update the session cart.
            sessionCartService.addToCartInSession(session, itemDto.getId(), itemDto.getTitle(), effectivePrice, quantity, itemDto.getImageUrl(), itemDto.getPrice());
        
        } catch (Exception e) {
            // Log the error for debugging purposes.
            System.err.println("Failed to add item to persistent cart for user " + userId + ". Error: " + e.getMessage());
            e.printStackTrace();
            
            // The method must still throw an exception to inform the calling servlet of the failure.
            throw new Exception("Failed to save cart data. Please try again.", e);
        }
    }

    public void loadCartForUser(HttpSession session, String customerId) throws Exception {
        sessionCartService.loadCartForUser(session, customerId);
    }

    public void removeCartItemAndRefreshSession(String customerId, String itemId, HttpSession session) throws Exception {
        persistentCartService.removeCartItem(customerId, itemId);
        sessionCartService.removeCartItemFromSession(session, itemId);
    }
    
    public void updateCartItem(String customerId, String itemId, int quantity, HttpSession session) throws Exception {
        if (quantity <= 0) {
            persistentCartService.removeCartItem(customerId, itemId);
            sessionCartService.removeCartItemFromSession(session, itemId);
        } else {
            persistentCartService.updateCartItemQuantity(customerId, itemId, quantity);
            sessionCartService.updateCartItemQuantityInSession(session, itemId, quantity);
        }
    }
    
     public boolean isItemInCart(String customerId, String itemId) throws Exception {
        CartItem item = persistentCartService.findByCustomerAndItem(customerId, itemId);
        return item != null;
    }
}