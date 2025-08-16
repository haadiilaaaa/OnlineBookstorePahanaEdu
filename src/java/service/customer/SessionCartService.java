// File: service/customer/SessionCartService.java
package service.customer;

import dto.ItemDTO;
import java.math.BigDecimal;
import javax.servlet.http.HttpSession;

public interface SessionCartService {
    void loadCartForUser(HttpSession session, String customerId) throws Exception;
    void addToCartInSession(HttpSession session, String itemId, String title, BigDecimal price, int quantity, String imageUrl, BigDecimal originalPrice);
    void updateCartItemQuantityInSession(HttpSession session, String itemId, int quantity);
    void removeCartItemFromSession(HttpSession session, String itemId);
    void clearCartInSession(HttpSession session);
}