package service.customer.discount;

import model.Discount;
import model.Item;

import java.math.BigDecimal;

public class PercentageDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscountedPrice(Item item, Discount discount) {
        BigDecimal originalPrice = item.getPrice();
        BigDecimal discountPercent = BigDecimal.valueOf(discount.getDiscountPercent());
        BigDecimal discountAmount = originalPrice.multiply(discountPercent).divide(BigDecimal.valueOf(100));
        return originalPrice.subtract(discountAmount);
    }
}
