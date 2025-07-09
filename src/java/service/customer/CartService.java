// CartService.java (interface)
package service.customer;

import model.CartItem;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.math.BigDecimal;

public interface CartService {
    Map<String, CartItem> getCartMapByCustomerId(String customerId) throws Exception;

    void loadCartForUser(HttpSession session, String customerId) throws Exception;
      void addCartItem(String customerId, String itemId, int quantity, BigDecimal price) throws Exception;
}
