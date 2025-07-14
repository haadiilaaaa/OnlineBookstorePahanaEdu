package dao;

import model.CartItem;
import model.Category;
import java.math.BigDecimal;
import java.util.List;
//cart item dao
public interface CartItemDAO {

    /**
     * Saves a new CartItem to the database.
     *
     * @param cartItem the cart item to save
     * @throws Exception if a database error occurs
     */
    void save(CartItem cartItem) throws Exception;

    /**
     * Gets the latest cart item ID from the database to assist in generating a new one.
     *
     * @return the highest cart item ID or null if none found
     * @throws Exception if a database error occurs
     */
   int getMaxCartItemNumber() throws Exception;

    /**
     * Finds all cart items associated with a specific customer ID.
     *
     * @param customerId the ID of the customer
     * @return a list of cart items belonging to the customer
     * @throws Exception if a database error occurs
     */
    List<CartItem> findByCustomerId(String customerId) throws Exception;

    /**
     * Calculates the total value of the customer's cart.
     *
     * @param customerId the ID of the customer
     * @return the total cart value as a BigDecimal
     * @throws Exception if a database error occurs
     */
    BigDecimal getCartTotal(String customerId) throws Exception;
void updateQuantity(String customerId, String itemId, int quantity) throws Exception;
void deleteByCustomerAndItem(String customerId, String itemId) throws Exception;
void deleteCartItemsByUserId(String userId) throws Exception;

// In CartItemDAO.java interface

void addCartItem(String customerId, String itemId, int quantity, BigDecimal price) throws Exception;



    
    

    
}
