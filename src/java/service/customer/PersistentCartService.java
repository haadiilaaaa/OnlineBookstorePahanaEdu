// File: service/customer/PersistentCartService.java
package service.customer;

import model.CartItem;
import java.math.BigDecimal;
import java.util.Map;

public interface PersistentCartService {
    Map<String, CartItem> getCartMapByCustomerId(String customerId) throws Exception;
    void addCartItem(String customerId, String itemId, int quantity, BigDecimal price) throws Exception;
    void updateCartItemQuantity(String customerId, String itemId, int quantity) throws Exception;
    void removeCartItem(String customerId, String itemId) throws Exception;
    void clearCart(String customerId) throws Exception;
     // Add the new method
    CartItem findByCustomerAndItem(String customerId, String itemId) throws Exception;
}