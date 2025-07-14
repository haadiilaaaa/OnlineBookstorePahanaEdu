package service.customer.discount;

import model.Discount;
import model.Item;

import java.math.BigDecimal;

public class AmountDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscountedPrice(Item item, Discount discount) {
        BigDecimal originalPrice = item.getPrice();
        BigDecimal discountAmount = BigDecimal.valueOf(discount.getDiscountPercent()); // assuming this stores fixed amount
        BigDecimal discountedPrice = originalPrice.subtract(discountAmount);
        return discountedPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPrice;  // Avoid negative price
    }
}
