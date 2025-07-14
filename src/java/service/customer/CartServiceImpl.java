package service.customer;

import dao.CartItemDAO;
import dto.ItemDTO;
import mapper.ItemMapper;
import model.CartItem;
import model.Item;
import util.IDGenerator;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartServiceImpl implements CartService {

    private final CartItemDAO cartItemDAO;
    private final SessionCartManager sessionCartManager;
    private final DiscountService discountService;
    private final ItemMapper itemMapper;

    public CartServiceImpl(CartItemDAO cartItemDAO, SessionCartManager sessionCartManager, DiscountService discountService, ItemMapper itemMapper) {
        this.cartItemDAO = cartItemDAO;
        this.sessionCartManager = sessionCartManager;
        this.discountService = discountService;
        this.itemMapper = itemMapper;
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
        if (session.getAttribute(SessionCartManager.CART_SESSION_KEY) == null) {
            Map<String, CartItem> cartMap = getCartMapByCustomerId(customerId);
            session.setAttribute(SessionCartManager.CART_SESSION_KEY, cartMap);
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
    public void addItem(String userId, ItemDTO itemDto, int quantity, HttpSession session) throws Exception {
        Item item = itemMapper.toItem(itemDto);  // Convert to Item
        BigDecimal effectivePrice = discountService.applyBestDiscount(item); // Use domain model

        sessionCartManager.addItemToCart(
                session,
                itemDto.getId(),
                itemDto.getTitle(),
                effectivePrice,
                quantity,
                itemDto.getImageUrl(),
                itemDto.getPrice()
        );

        int nextId = cartItemDAO.getMaxCartItemNumber() + 1;
        String cartItemId = IDGenerator.generateId("cart", nextId);

        CartItem cartItem = new CartItem(cartItemId, userId, itemDto.getId(), quantity);
        cartItem.setPrice(effectivePrice);

        cartItemDAO.save(cartItem);
    }
}
