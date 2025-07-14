package service.customer.discount;

import model.Discount;
import model.Item;
import java.math.BigDecimal;

public interface DiscountStrategy {
    /**
     * Calculate the discounted price based on the discount type and original item price.
     *
     * @param item The item to discount.
     * @param discount The discount info.
     * @return The discounted price.
     */
    BigDecimal calculateDiscountedPrice(Item item, Discount discount);
}
