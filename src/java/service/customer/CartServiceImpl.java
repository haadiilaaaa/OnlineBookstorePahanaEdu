package service.customer;

import dao.CartItemDAO;
import model.CartItem;
import util.IDGenerator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.*;
import java.math.BigDecimal;

public class CartServiceImpl implements CartService {

    private final CartItemDAO cartItemDAO;

    public CartServiceImpl(CartItemDAO cartItemDAO) {
        this.cartItemDAO = cartItemDAO;
    }

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
    
    @Override
    public void loadCartForUser(HttpSession session, String customerId) throws Exception {
        if (session.getAttribute("cart") == null) {
            Map<String, CartItem> cartMap = getCartMapByCustomerId(customerId);
            session.setAttribute("cart", cartMap);
        }
    }
    
    @Override
public void addCartItem(String customerId, String itemId, int quantity, BigDecimal price) throws Exception {
    int nextId = cartItemDAO.getMaxCartItemNumber() + 1;
    String cartItemId = IDGenerator.generateId("cart", nextId);
    CartItem dbItem = new CartItem(cartItemId, customerId, itemId, quantity);
    dbItem.setPrice(price);
    cartItemDAO.save(dbItem);
}

}
