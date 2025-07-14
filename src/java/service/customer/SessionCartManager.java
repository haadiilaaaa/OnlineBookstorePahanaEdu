package service.customer;

import model.CartItem;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SessionCartManager {

    public static final String CART_SESSION_KEY = "cart";

    @SuppressWarnings("unchecked")
    public Map<String, CartItem> getCart(HttpSession session) {
        Object cartObj = session.getAttribute(CART_SESSION_KEY);
        if (cartObj instanceof Map) {
            return (Map<String, CartItem>) cartObj;
        }
        Map<String, CartItem> newCart = new HashMap<>();
        session.setAttribute(CART_SESSION_KEY, newCart);
        return newCart;
    }

    public void addItemToCart(HttpSession session, String itemId, String title, BigDecimal price, int quantity, String imageUrl, BigDecimal originalPrice) {
        Map<String, CartItem> cart = getCart(session);
        CartItem cartItem = cart.get(itemId);
        if (cartItem == null) {
            cartItem = new CartItem(itemId, title, price, quantity);
            cartItem.setImageUrl(imageUrl);
            cartItem.setOriginalPrice(originalPrice);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(price);
        }
        cart.put(itemId, cartItem);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateItemQuantity(HttpSession session, String itemId, int quantity) {
        Map<String, CartItem> cart = getCart(session);
        CartItem cartItem = cart.get(itemId);
        if (cartItem != null) {
            if (quantity <= 0) {
                cart.remove(itemId);
            } else {
                cartItem.setQuantity(quantity);
            }
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeItemFromCart(HttpSession session, String itemId) {
        Map<String, CartItem> cart = getCart(session);
        cart.remove(itemId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}
