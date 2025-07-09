package service.customer;

import model.Item;
import java.math.BigDecimal;

/**
 * Applies the best available discount to an item, updates the item metadata, and returns the discounted price.
 */
public interface DiscountService {

    /**
     * Applies the best discount available for the given item and updates its discount metadata.
     *
     * @param item the item to apply discount to
     * @return the discounted price (or original price if no discount applies)
     * @throws Exception if something goes wrong during discount lookup
     */
    BigDecimal applyBestDiscount(Item item) throws Exception;
}
